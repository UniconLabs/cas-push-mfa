package org.apereo.cas.pushmfa;

/**
 * This is {@link AcknowledgePushRequest}.
 *
 * @author John Gasper
 * @since 5.2.9
 */
public class AcknowledgePushRequest {

    private String nonce;

    private String token;

    public String getNonce() {
        return nonce;
    }

    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}
