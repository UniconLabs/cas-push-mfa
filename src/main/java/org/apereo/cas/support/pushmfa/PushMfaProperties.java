package org.apereo.cas.support.pushmfa;

import java.io.Serializable;

/**
 * This is {@link org.apereo.cas.support.pushmfa.PushMfaProperties}.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */
public class PushMfaProperties implements Serializable {

    /**
     *
     */
    private boolean useDevServer;

    /**
     *
     */
    private String bundleId;

    /**
     *
     */
    private ClientCredential clientCredential = new ClientCredential();

    /**
     *
     */
    private SigningKey signingKey = new SigningKey();


    public boolean getUseDevServer() {
        return useDevServer;
    }

    public void setUseDevServer(boolean useDevServer) {
        this.useDevServer = useDevServer;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public void setClientcredential(ClientCredential clientCredential) {
        this.clientCredential = clientCredential;
    }

    public ClientCredential getClientcredential() {
        return clientCredential;
    }

    public SigningKey getSigningkey() {
        return signingKey;
    }

    public void setSigningkey(SigningKey signingKey) {
        this.signingKey = signingKey;
    }

    /**
     *
     */
    public class ClientCredential implements Serializable {

        /**
         *
         */
        private String p12CertPath;


        /**
         *
         */
        private String p12Password;


        public String getP12CertPath() {
            return p12CertPath;
        }

        public void setP12CertPath(String p12CertPath) {
            this.p12CertPath = p12CertPath;
        }

        public String getP12Password() {
            return p12Password;
        }

        public void setP12Password(String p12Password) {
            this.p12Password = p12Password;
        }
    }

    /**
     *
     */
    public class SigningKey implements Serializable {

        /**
         *
         */
        private String pkcs8File;

        /**
         *
         */
        private String teamId;

        /**
         *
         */
        private String keyId;


        public String getPkcs8File() {
            return pkcs8File;
        }

        public void setPkcs8File(String pkcs8File) {
            this.pkcs8File = pkcs8File;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }
    }
}
