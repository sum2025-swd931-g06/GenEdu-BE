package com.genedu.media.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfig {
    private static final String PROJECT_SERVICE_URL = "http://project-service/api/v1";

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("projectWebClient")
    public WebClient projectWebClient(WebClient.Builder builder) {
        return builder.baseUrl(PROJECT_SERVICE_URL).build();
    }
}
