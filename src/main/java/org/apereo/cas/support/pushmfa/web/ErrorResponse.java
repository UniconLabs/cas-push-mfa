package org.apereo.cas.support.pushmfa.web;

/**
 * This is {@link ErrorResponse}.
 *
 * @author John Gasper
 * @since 5.2.9
 */
public class ErrorResponse {

    private String message;

    public ErrorResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message){
        this.message = message;
    }
}
