package org.apereo.cas.pushmfa;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is {@link AcknowledgePushRequest}.
 *
 * @author John Gasper
 * @since 5.2.9
 */
public class AcknowledgePushRequest {
    //@JsonProperty("nonce")
    private String nonce;

    @JsonProperty("auth_code")
    private String authCode;

    public String getNonce() {
        return nonce;
    }

    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(final String authCode) {
        this.authCode = authCode;
    }
}
