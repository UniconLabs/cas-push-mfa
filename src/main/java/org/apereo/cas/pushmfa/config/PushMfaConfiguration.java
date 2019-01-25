package org.apereo.cas.pushmfa.config;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.pushmfa.web.PushMfaEndpointController;
import org.apereo.cas.ticket.registry.TicketRegistry;
import org.apereo.cas.util.DefaultUniqueTicketIdGenerator;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 */
@Configuration("pushMfaConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class PushMfaConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("ticketRegistry")
    private TicketRegistry ticketRegistry;

    @Autowired
    @Qualifier("auditTrailManager")
    private AuditTrailManager auditTrailManager;

    @RefreshScope
    @Bean
    public PushMfaEndpointController pushMfaEndpointController() {
        return new PushMfaEndpointController(casProperties, ticketRegistry, new DefaultUniqueTicketIdGenerator(10, "test"), auditTrailManager);
    }
}