package org.apereo.cas.pushmfa;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apereo.cas.ticket.Ticket;

/**
 * Interface for a Service Ticket. A service ticket is used to grant access to a
 * specific service for a principal. A Service Ticket is generally a one-time
 * use ticket.
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
     * Retrieve the service this ticket was given for.
     *
     * @return the server.
     */
    String getToken();

    /**
     *
     * @param token
     */
    void setToken(String token);

}

