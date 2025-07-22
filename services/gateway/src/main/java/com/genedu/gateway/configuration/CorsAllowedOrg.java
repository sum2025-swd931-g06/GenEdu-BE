package com.genedu.gateway.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "custom-cors")
public record CorsAllowedOrg(
        List<String> allowedOrigins
) {
}
