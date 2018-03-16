package com.microservices.configuration;

import com.microservices.GenericFallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Created by mihir.desai on 3/15/2018.
 */
@Configuration
public class HystrixFallbackProvider {
    @Bean
    public FallbackProvider zuulFallbackProvider() {
       return new GenericFallbackProvider();
    }

}
