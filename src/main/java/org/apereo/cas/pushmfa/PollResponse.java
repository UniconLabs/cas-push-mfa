package org.apereo.cas.pushmfa;

/**
 * This is {@link PollResponse}.
 *
 * @author John Gasper
 * @since 5.2.9
 */
public class PollResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}
