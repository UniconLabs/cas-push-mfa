package org.apereo.cas.support.pushmfa.config;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.support.pushmfa.notifications.NotificationService;
import org.apereo.cas.support.pushmfa.web.PushMfaEndpointController;
import org.apereo.cas.support.pushmfa.utils.PushMfaUtils;
import org.apereo.cas.ticket.registry.TicketRegistry;
import org.apereo.cas.util.DefaultUniqueTicketIdGenerator;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is {@link PushMfaConfiguration}.
 *
 * @author John Gasper
 * @since 5.2.9
 */
@Configuration("pushMfaConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class PushMfaConfiguration  {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("ticketRegistry")
    private ObjectProvider<TicketRegistry> ticketRegistry;

    @Autowired
    @Qualifier("auditTrailManager")
    private ObjectProvider<AuditTrailManager> auditTrailManager;

    @Bean
    public PushMfaUtils pushMfaUtils() {
        return new PushMfaUtils();
    }

    @RefreshScope
    @Bean
    public PushMfaEndpointController pushMfaEndpointController() {
        return new PushMfaEndpointController(casProperties, ticketRegistry.getIfAvailable(),
                new DefaultUniqueTicketIdGenerator(10, "test"),
                auditTrailManager.getIfAvailable(),
                new NotificationService(casProperties.getAuthn().getMfa().getPush()));
    }
}