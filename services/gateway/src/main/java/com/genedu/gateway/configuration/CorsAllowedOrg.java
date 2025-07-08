package com.genedu.gateway.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("custom-cors.allowed-origins")
public record CorsAllowedOrg(
        List<String> corsAllowedOrigins
) {
}
