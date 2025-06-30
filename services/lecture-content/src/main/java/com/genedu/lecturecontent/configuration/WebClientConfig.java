package com.genedu.lecturecontent.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfig {
    private static final String LECTURE_MEDIA_SERVICE_URL = "http://media-service/api/v1";

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("lectureMediaWebClient")
    public WebClient lectureMediaWebClient(WebClient.Builder builder) {
        return builder.baseUrl(LECTURE_MEDIA_SERVICE_URL).build();
    }
}
