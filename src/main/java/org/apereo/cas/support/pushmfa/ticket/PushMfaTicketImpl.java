package org.apereo.cas.support.pushmfa.ticket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apereo.cas.authentication.Authentication;

import org.apereo.cas.ticket.AbstractTicket;
import org.apereo.cas.ticket.ExpirationPolicy;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Domain object representing a PushMfa Ticket. A PushMfa ticket stores the OTP
 * sent from a user's device for retrieval by the user's browser,
 * Generally, it is a one time use Ticket.
 *
 * @author John Gasper
 * @since 5.2.9
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
     * The OTP
     */
    @Column(name = "TOKEN", nullable = true)
    private String token;

    /**
     * The Principal
     */
    @Column(name = "PRINCIPAL", nullable = false)
    private String principal;


    /**
     * Instantiates a new PushMfa ticket impl.
     */
    public PushMfaTicketImpl() {
        // exists for JPA purposes
    }

    /**
     * Constructs a new PushMfaTicket with a Unique Id and Expiration Policy
     *
     * @param id                 the unique identifier for the ticket.
     * @param policy             the expiration policy for the Ticket.
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

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
