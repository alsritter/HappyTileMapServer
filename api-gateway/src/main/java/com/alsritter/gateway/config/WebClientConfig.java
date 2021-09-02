package com.alsritter.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final SecureProperties.TokenConfig tokenConfig;

    @Bean
    @LoadBalanced
    public WebClient.Builder webClient() {
        return WebClient.builder()
                .defaultHeaders(h -> h.setBasicAuth(tokenConfig.getClientId(), tokenConfig.getClientSecret()));
    }
}
