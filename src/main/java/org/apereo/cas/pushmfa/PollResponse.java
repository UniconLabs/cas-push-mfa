package org.apereo.cas.pushmfa;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is {@link PollResponse}.
 *
 * @author John Gasper
 * @since 5.2.9
 */
public class PollResponse {
    @JsonProperty("auth_code")
    private String authCode;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(final String authCode) {
        this.authCode = authCode;
    }
}
