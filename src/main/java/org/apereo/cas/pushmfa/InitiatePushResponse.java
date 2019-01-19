package org.apereo.cas.pushmfa;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is {@link InitiatePushResponse}.
 *
 * @author John Gasper
 * @since 5.2.9
 */
public class InitiatePushResponse {
    private String nonce;

    public String getNonce() {
        return nonce;
    }

    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }
}