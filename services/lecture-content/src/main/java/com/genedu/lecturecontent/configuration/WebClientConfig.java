package com.genedu.lecturecontent.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
//    private static final String lectureMediaServiceUrl = "http://media-service/api/v1";
//    private static final String projectServiceUrl = "http://project-service/api/v1";
//    private static final String contentServiceUrl = "http://project-service/api/v1";
//
//    @Bean(name = "lectureMediaWebClient")
//    @LoadBalanced
//    public WebClient.Builder webClientBuilder() {
//        return WebClient.builder()
//                .baseUrl(lectureMediaServiceUrl);
//    }
//
//    @Bean(name = "projectWebClient")
//    @LoadBalanced
//    public WebClient.Builder projectWebClientBuilder() {
//        return WebClient.builder()
//                .baseUrl(projectServiceUrl);
//    }
//
//    @Bean(name = "contentWebClient")
//    @LoadBalanced
//    public WebClient.Builder contentWebClientBuilder() {
//        return WebClient.builder()
//                .baseUrl(contentServiceUrl);
//    }
}
