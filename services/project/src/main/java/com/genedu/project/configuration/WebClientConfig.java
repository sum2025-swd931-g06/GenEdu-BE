package com.genedu.project.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private static final String lectureMediaServiceUrl = "http://media-service/api/v1";

    @Bean(name = "lectureMediaWebClient")
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(lectureMediaServiceUrl);
    }
}
