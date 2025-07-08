package com.genedu.notification.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Gate Way Service API", description = "Gate Way API documentation", version = "1.0"),
        security = @SecurityRequirement(name = "keycloak"),
        servers = {
                @Server(url = "${GATEWAY_SERVICE_URL}", description = "Gateway server")
        }
)
@SecurityScheme(
        name = "keycloak",
        scheme = "bearer",
        type = SecuritySchemeType.OPENIDCONNECT,
        in = SecuritySchemeIn.HEADER,
        openIdConnectUrl = "${OPENID_CONFIG_URL}"
)
public class SwaggerConfig {
}
