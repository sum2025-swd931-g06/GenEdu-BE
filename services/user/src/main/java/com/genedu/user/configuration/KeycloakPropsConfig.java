package com.genedu.user.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakPropsConfig {
    private String serverUrl;
    private String realm;
    private String clientId;
    private String grantType;
    private String username;
    private String password;
    private String clientSecret;
}
