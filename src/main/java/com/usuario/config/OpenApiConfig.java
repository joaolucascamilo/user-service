package com.usuario.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("Serviço de gerenciamento de usuários e gamificação da plataforma de infraestrutura urbana. " +
                                "Responsável por autenticação JWT, cadastro de cidadãos e agentes da prefeitura, " +
                                "e registro de pontos de reputação.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TCC - Infraestrutura Urbana")
                                .email("lucascamilo373@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Informe o token JWT obtido em /api/auth/login. Exemplo: Bearer eyJhbGci...")));
    }
}
