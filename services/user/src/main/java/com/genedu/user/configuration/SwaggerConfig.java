package com.genedu.user.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;

@OpenAPIDefinition(
        info = @Info(title = "Gate Way Service API", description = "Gate Way API documentation", version = "1.0"),
        security = @SecurityRequirement(name = "keycloak"
        )
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
