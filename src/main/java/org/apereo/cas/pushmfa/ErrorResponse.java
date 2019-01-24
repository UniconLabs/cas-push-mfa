package org.apereo.cas.pushmfa;

/**
 *
 */
public class ErrorResponse {

    /**
     *
     */
    private String message;

    public ErrorResponse(final String message) {
        this.message = message;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(final String message){
        this.message = message;
    }
}
