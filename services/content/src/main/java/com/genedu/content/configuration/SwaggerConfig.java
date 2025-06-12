package com.genedu.content.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Gate Way Service API", description = "Gate Way API documentation", version = "1.0"),
        security = @SecurityRequirement(name = "keycloak"),
        servers = {
                @Server(url = "http://localhost:8222", description = "Gateway server")
        }
)
@SecurityScheme(
        name = "keycloak",
        scheme = "bearer",
        type = SecuritySchemeType.OPENIDCONNECT,
        in = SecuritySchemeIn.HEADER,
        openIdConnectUrl = "http://localhost:9099/realms/GenEdu/.well-known/openid-configuration"
)
public class SwaggerConfig {
}
