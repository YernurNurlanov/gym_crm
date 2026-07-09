package com.crm.gym.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "Bearer Authentication")
)
public class OpenApiConfig {

    @Bean
    public OpenAPI gymApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Gym CRM API")
                        .description("Gym CRM Management System")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Gym CRM Team")));
    }
}