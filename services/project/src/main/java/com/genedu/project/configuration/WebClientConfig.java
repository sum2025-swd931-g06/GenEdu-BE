package com.genedu.project.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private static final String mediaServiceUrl = "http://media-service/api/v1";

    @Bean(name = "mediaWebClient")
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(mediaServiceUrl);
    }
}
