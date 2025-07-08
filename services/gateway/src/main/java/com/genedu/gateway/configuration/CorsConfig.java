package com.genedu.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig extends CorsConfiguration {
    @Autowired
    CorsAllowedOrg corsAllowedOrg;

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
        corsConfiguration.setAllowedOrigins(corsAllowedOrg.corsAllowedOrigins());
        corsConfiguration.setAllowedMethods(Arrays.asList(ALLOWED_METHODS));
        corsConfiguration.setAllowedHeaders(Arrays.asList(ALLOWED_HEADERS));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
