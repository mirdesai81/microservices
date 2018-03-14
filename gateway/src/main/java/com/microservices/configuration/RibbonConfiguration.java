package com.microservices.configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.context.annotation.Bean;

/**
 * Created by mihir.desai on 3/13/2018.
 */
public class RibbonConfiguration {

    @Bean
    public IPing ribbonPing(final IClientConfig
                            iClientConfig) {
        return new PingUrl(false, "/health");
    }

    @Bean
    public IRule ribbonRule(final IClientConfig iClientConfig) {
        return new AvailabilityFilteringRule();
    }
}
