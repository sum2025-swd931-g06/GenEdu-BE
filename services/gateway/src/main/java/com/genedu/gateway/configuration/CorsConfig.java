package com.genedu.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig extends CorsConfiguration {

    private static final String[] ALLOWED_ORIGINS = {
            "http://localhost:63342",
            "http://localhost:5173",
            "https://localhost:8443",
            "http://localhost:4000",
    };

    private static final String[] ALLOWED_METHODS = {
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"
    };

    private static final String[] ALLOWED_HEADERS = {
            "Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin"
    };

    @Bean
    public CorsWebFilter corsFilter() {
        org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList(ALLOWED_ORIGINS));
        corsConfiguration.setAllowedMethods(Arrays.asList(ALLOWED_METHODS));
        corsConfiguration.setAllowedHeaders(Arrays.asList(ALLOWED_HEADERS));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
