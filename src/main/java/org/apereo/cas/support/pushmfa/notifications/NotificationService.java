package org.apereo.cas.support.pushmfa.notifications;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.auth.ApnsSigningKey;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import com.turo.pushy.apns.util.concurrent.PushNotificationResponseListener;
import io.netty.util.concurrent.Future;
import org.apache.commons.lang.StringUtils;
import org.apereo.cas.support.pushmfa.PushMfaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private ApnsClient apnsClient = null;

    private final PushMfaProperties pushMfaProperties;

    public  NotificationService(PushMfaProperties pushMfaProperties) {
        this.pushMfaProperties = pushMfaProperties;

        final String apnsServer = pushMfaProperties.getUseDevServer() ? ApnsClientBuilder.DEVELOPMENT_APNS_HOST : ApnsClientBuilder.PRODUCTION_APNS_HOST;

        if (pushMfaProperties.getClientcredential() != null &&
                StringUtils.isNotEmpty(pushMfaProperties.getClientcredential().getP12CertPath()) &&
                StringUtils.isNotEmpty(pushMfaProperties.getClientcredential().getP12Password())) {
            try {
                apnsClient = new ApnsClientBuilder()
                        .setApnsServer(apnsServer)
                        .setClientCredentials(new File(pushMfaProperties.getClientcredential().getP12CertPath().trim()),
                                pushMfaProperties.getClientcredential().getP12Password().trim())
                        .build();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (pushMfaProperties.getSigningkey() != null &&
                StringUtils.isNotEmpty(pushMfaProperties.getSigningkey().getPkcs8File()) &&
                StringUtils.isNotEmpty(pushMfaProperties.getSigningkey().getKeyId()) &&
                StringUtils.isNotEmpty(pushMfaProperties.getSigningkey().getTeamId())) {
            try {
                apnsClient = new ApnsClientBuilder()
                        .setApnsServer(apnsServer)
                        .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(pushMfaProperties.getSigningkey().getPkcs8File().trim()),
                                pushMfaProperties.getSigningkey().getTeamId().trim(), pushMfaProperties.getSigningkey().getKeyId().trim()))
                        .build();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNotification(final String data) {
        if (apnsClient == null) {
            LOGGER.info("Sending mock message: {}", data);
            return;
        }

        final SimpleApnsPushNotification pushNotification;

        final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        payloadBuilder.setAlertBody(data);
        payloadBuilder.setAlertTitle(data + "2");
        payloadBuilder.setCategoryName("CAS_APPROVE_DENY_CATEGORY");

        final String payload = payloadBuilder.buildWithDefaultMaximumLength();
        final String deviceToken = TokenUtil.sanitizeTokenString("deviceTokenIdHere");

        LOGGER.debug("Creating notification for {} with {} and {}", deviceToken, pushMfaProperties.getBundleId().trim(), payload);
        pushNotification = new SimpleApnsPushNotification(deviceToken, pushMfaProperties.getBundleId().trim(), payload);

        final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        sendNotificationFuture.addListener(new PushNotificationResponseListener<SimpleApnsPushNotification>() {

            @Override
            public void operationComplete(final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future) throws Exception {
                // When using a listener, callers should check for a failure to send a
                // notification by checking whether the future itself was successful
                // since an exception will not be thrown.
                if (future.isSuccess()) {
                    final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                            sendNotificationFuture.getNow();

                    LOGGER.debug("APNS Response {}, {}", pushNotificationResponse.isAccepted(), pushNotificationResponse.getApnsId());
                } else {
                    LOGGER.error("Something unexpected occurred sending the APNS message", future.cause());
                }
            }
        });
    }

    @PreDestroy
    public void clearApnsClient() {
        if (apnsClient != null) {
            try {
                Future<Void> closeFuture = apnsClient.close();
                closeFuture.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
