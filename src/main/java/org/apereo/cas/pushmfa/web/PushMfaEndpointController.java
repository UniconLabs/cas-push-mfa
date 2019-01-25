package org.apereo.cas.pushmfa.web;

import org.apache.commons.lang.StringUtils;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.pushmfa.*;
import org.apereo.cas.ticket.UniqueTicketIdGenerator;
import org.apereo.cas.ticket.registry.TicketRegistry;
import org.apereo.cas.ticket.support.HardTimeoutExpirationPolicy;
import org.apereo.cas.util.DateTimeUtils;
import org.apereo.inspektr.audit.AuditActionContext;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.apereo.inspektr.common.web.ClientInfo;
import org.apereo.inspektr.common.web.ClientInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.ForkJoinPool;

/**
 * This is {@link PushMfaEndpointController}.
 *
 * @author John Gasper
 * @since 5.2.9
 */

@Controller
@RequestMapping("/pushmfa")
public class PushMfaEndpointController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMfaEndpointController.class);

    private final CasConfigurationProperties casProperties;

    private final TicketRegistry ticketRegistry;

    private final UniqueTicketIdGenerator uniqueTicketIdGenerator;

    private final AuditTrailManager auditTrailManager;

    /**
     * Instantiates a new mvc endpoint.
     *
     * @param casProperties             the cas properties
     */
    public PushMfaEndpointController(final CasConfigurationProperties casProperties,
                                     final TicketRegistry ticketRegistry,
                                     final UniqueTicketIdGenerator uniqueTicketIdGenerator,
                                     final AuditTrailManager auditTrailManager) {

        LOGGER.info("PushMfaEndpointController initialized.");

        this.casProperties = casProperties;
        this.ticketRegistry = ticketRegistry;
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
        this.auditTrailManager = auditTrailManager;
    }


    /**
     *
     * @return
     */
    @GetMapping(value = {"initiate"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InitiatePushResponse> initiate() {
        InitiatePushResponse response = new InitiatePushResponse();

        //Validate user session

        String pushMfaTicketId = uniqueTicketIdGenerator.getNewTicketId(PushMfaTicket.PREFIX);
        LOGGER.debug("New Push MFA request for {} with Id: {}", "unknown", pushMfaTicketId);

        PushMfaTicket ticket = new PushMfaTicketImpl(pushMfaTicketId, new HardTimeoutExpirationPolicy(120));
        ticketRegistry.addTicket(ticket);

        recordAuditEvent("user", pushMfaTicketId, "PUSHMFA_INITIATED");

        //Perhaps sign the payload (JWT?)
        //Send signed payload to notification service

        LOGGER.debug("Returning Id for polling: {}", pushMfaTicketId);
        response.setNonce(pushMfaTicketId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param requestEntity
     * @return
     */
    @PostMapping(value = {"acknowledge"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcknowledgePushResponse> acknowledge(RequestEntity<AcknowledgePushRequest> requestEntity) {

        AcknowledgePushRequest request = requestEntity.getBody();
        LOGGER.debug("Acknowledgement received for {} with authcode {}", request.getNonce(), request.getAuthCode());
        recordAuditEvent("user", request.getNonce(), "PUSHMFA_SHARED");

        PushMfaTicket ticket = ticketRegistry.getTicket(request.getNonce(), PushMfaTicket.class);

        if (ticket == null) {
            LOGGER.warn("Ticket ({}) not found", request.getNonce());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ticket.setToken(request.getAuthCode());
        ticketRegistry.updateTicket(ticket);
        LOGGER.debug("Ticket ({}) updated with authcode", request.getNonce());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param requestEntity
     * @return
     */
    @PostMapping(value = {"poll"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<PollResponse>> poll(RequestEntity<PollRequest> requestEntity) {

        //TODO: Validate user session;

        PollRequest request = requestEntity.getBody();

        DeferredResult<ResponseEntity<PollResponse>> result = new DeferredResult<>();
        result.onTimeout(() -> {
            LOGGER.debug("Request timeout for {}, returning 408... Client may try request again", request.getNonce());
            result.setErrorResult(
                    ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                            .body(new ErrorResponse("Request timeout occurred.")));
        });


        ForkJoinPool.commonPool().submit(() -> {
            LOGGER.debug("Polling for ticket response from: {}", request.getNonce());

            try {
                PushMfaTicket ticket = ticketRegistry.getTicket(request.getNonce(), PushMfaTicket.class);

                while (ticket != null && ticket.getToken() == null) {
                    LOGGER.trace("No authCode found for ticket (sleeping 1s): {}", request.getNonce());

                    Thread.sleep(1000);
                    ticket = ticketRegistry.getTicket(request.getNonce(), PushMfaTicket.class);
                }

                if (ticket == null) {
                    LOGGER.debug("Ticket not found: {}, returning 404", request.getNonce());
                    result.setErrorResult(
                            ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(new ErrorResponse("Item Not Found")));

                } else {
                    LOGGER.info("auth_code found for {}, returning: {}", request.getNonce(), ticket.getToken());
                    recordAuditEvent("user", request.getNonce(), "PUSHMFA_RETRIEVED");

                    final PollResponse response = new PollResponse();
                    response.setAuthCode(ticket.getToken());

                    result.setResult(new ResponseEntity<>(response, HttpStatus.OK));
                }
            } catch (InterruptedException e) {
                LOGGER.debug("Execution interrupted for {}: {}", request.getNonce(), e.getMessage());
            }
        });


        return result;
    }

    /**
     *
     * @param user
     * @param resource
     * @param action
     */
    private void recordAuditEvent(final String user, final String resource, final String action ) {
        final ClientInfo clientInfo = ClientInfoHolder.getClientInfo();

        String clientIp = "Unknown",
                serverIp = "Unknown";

        if (clientInfo != null) {
            clientIp = clientInfo.getClientIpAddress();
            serverIp = clientInfo.getServerIpAddress();
        }

        auditTrailManager.record(new AuditActionContext(user, resource,
                action, "CAS", DateTimeUtils.dateOf(ZonedDateTime.now(ZoneOffset.UTC)),
                clientIp, serverIp));
    }
}
