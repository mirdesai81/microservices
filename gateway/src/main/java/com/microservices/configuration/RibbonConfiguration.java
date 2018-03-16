package com.microservices.configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mihir.desai on 3/13/2018.
 */
@Slf4j
public class RibbonConfiguration {



    @Bean
    public IPing ribbonPing(final IClientConfig
                            iClientConfig) {
        log.debug("Ribbon PingUrl called");
        return new PingUrl(false,"/application/health");
    }

    @Bean
    public IRule ribbonRule(final IClientConfig iClientConfig) {
        log.debug("Ribbon AvailabilityFilteringRule called");
        return new AvailabilityFilteringRule();
    }

    @Bean
    public ServerList<Server> ribbonServerList(final IClientConfig iClientConfig) {
        ConfigurationBasedServerList configServerList = new ConfigurationBasedServerList();
        configServerList.initWithNiwsConfig(iClientConfig);
        return configServerList;
    }

    @Bean
    public ServerListSubsetFilter serverListFilter() {
        return new ServerListSubsetFilter();
    }

}
