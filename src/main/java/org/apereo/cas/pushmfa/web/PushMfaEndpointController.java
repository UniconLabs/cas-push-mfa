package org.apereo.cas.pushmfa.web;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.pushmfa.*;
import org.apereo.cas.ticket.registry.TicketRegistry;
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

    /**
     * Instantiates a new mvc endpoint.
     *
     * @param casProperties             the cas properties
     */
    public PushMfaEndpointController(final CasConfigurationProperties casProperties,
                                     final TicketRegistry ticketRegistry) {
          this.casProperties = casProperties;
          this.ticketRegistry = ticketRegistry;
    }


    /**
     *
     * @return
     */
    @GetMapping(value = {"initiate"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InitiatePushResponse> initiate() {
        InitiatePushResponse response = new InitiatePushResponse();

        //Validate user session
        //Generate and store a nonce, like in the Ticket Registry
        //Generate a timestamp
        //Perhaps sign the payload (JWT?)
        //Send signed payload to notification service

        //Return the nonce to JSON so JavaScript can poll (not ideal but a good starter?

        response.setNonce("ABCDefg123");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     *
     * @param requestEntity
     * @return
     */
    @PostMapping(value = {"acknowledge"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AcknowledgePushResponse> acknowledge(RequestEntity<AcknowledgePushRequest> requestEntity) {

        AcknowledgePushRequest request = requestEntity.getBody();

        // Look up request.getNonce() from Ticket Registry
        // Set request.getAuthCode() in ticket
        // store ticket for polling
        // Return 200 to the device

        AcknowledgePushResponse response = new AcknowledgePushResponse();

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param requestEntity
     * @return
     */
    @PostMapping(value = {"poll"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PollResponse> poll(RequestEntity<PollRequest> requestEntity) {

        PollRequest request = requestEntity.getBody();

        // Look up request.getNonce() from Ticket Registry
        // Return the ticket's auth_code so JS can insert and submit the page.
        // Return 200

        PollResponse response = new PollResponse();
        response.setAuthCode("123456");
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
