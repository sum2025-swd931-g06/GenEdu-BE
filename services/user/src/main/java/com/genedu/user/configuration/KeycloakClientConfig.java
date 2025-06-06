package com.genedu.user.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;
import static org.keycloak.OAuth2Constants.PASSWORD;

@Configuration
public class KeycloakClientConfig {
    private final KeycloakPropsConfig keycloakPropsConfig;

    public KeycloakClientConfig(KeycloakPropsConfig keycloakPropsConfig) {
        this.keycloakPropsConfig = keycloakPropsConfig;
    }

    // The Keycloak instance can be accessed through the KeycloakSecurityUtil
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(CLIENT_CREDENTIALS)
                .serverUrl(keycloakPropsConfig.getServerUrl())
                .realm(keycloakPropsConfig.getRealm())
                .clientId(keycloakPropsConfig.getClientId())
                .clientSecret(keycloakPropsConfig.getClientSecret())
                .build();
    }
}