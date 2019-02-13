package org.apereo.cas.support.pushmfa.ticket;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apereo.cas.ticket.Ticket;

/**
 * Interface for a PushMfa Ticket. A PushMfa ticket is store the OTP from a
 * user's device. A PushMfa Ticket is generally a one-time use ticket.
 *
 * @author John Gasper
 * @since 5.2.9
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY)
public interface PushMfaTicket extends Ticket {

    /**
     * Prefix generally applied to unique ids generated
     * by UniqueTicketIdGenerator.
     */
    String PREFIX = "PMT";

    /**
     * Retrieve the OTP this ticket is storing.
     *
     * @return the token.
     */
    String getToken();

    /**
     *
     * @param token
     */
    void setToken(String token);

    /**
     * Retrieve the principal associated with this ticket.
     *
     * @return the principal.
     */
    String getPrincipal();

    /**
     *
     * @param principal
     */
    void setPrincipal(String principal);

}

