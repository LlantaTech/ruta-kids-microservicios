package org.pe.llantatech.keycloakservice.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI influyoWebServiceOpenApi() {

        OpenAPI open = new OpenAPI()
                .info(new Info()
                        .title("RutaKids Services")
                        .description("""
                                This API provides endpoints for user authentication microservice
                                """)
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addResponses("unauthorized", new ApiResponse()
                                .description("Unauthorized - You must provide a valid Bearer token"))
                        .addResponses("forbidden", new ApiResponse()
                                .description("Forbidden - You do not have permission to access this resource"))

                );

        open.servers(List.of(new Server().url("/")));
        return open;
    }
}
