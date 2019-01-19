package org.apereo.cas.pushmfa;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apereo.cas.authentication.Authentication;

import org.apereo.cas.ticket.AbstractTicket;
import org.apereo.cas.ticket.ExpirationPolicy;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Domain object representing a Service Ticket. A service ticket grants specific
 * access to a particular service. It will only work for a particular service.
 * Generally, it is a one time use Ticket, but the specific expiration policy
 * can be anything.
 *
 * @author Scott Battaglia
 * @since 3.0.0
 */
@Entity
@Table(name = "PUSHMFATICKET")
@DiscriminatorColumn(name = "TYPE")
@DiscriminatorValue(PushMfaTicket.PREFIX)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
public class PushMfaTicketImpl extends AbstractTicket implements PushMfaTicket {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMfaTicketImpl.class);

    private static final long serialVersionUID = -4423319704861765245L;


    /**
     * Is this service ticket the result of a new login?
     */
    @Column(name = "TOKEN", nullable = false)
    private String token;

    /**
     * Instantiates a new service ticket impl.
     */
    public PushMfaTicketImpl() {
        // exists for JPA purposes
    }

    /**
     * Constructs a new ServiceTicket with a Unique Id, a TicketGrantingTicket,
     * a Service, Expiration Policy and a flag to determine if the ticket
     * creation was from a new Login or not.
     *
     * @param id                 the unique identifier for the ticket.
     * @param policy             the expiration policy for the Ticket.
     * @throws IllegalArgumentException if the TicketGrantingTicket or the Service are null.
     */
    @JsonCreator
    public PushMfaTicketImpl(@JsonProperty("id")
                             final String id,
                             @JsonProperty("expirationPolicy")
                             final ExpirationPolicy policy) {
        super(id, policy);
    }


    @Override
    public Authentication getAuthentication() {
        return null;
    }


    @Override
    public TicketGrantingTicket getGrantingTicket() {
        return null;
    }

    @Override
    public String getPrefix() {
        return PushMfaTicket.PREFIX;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
