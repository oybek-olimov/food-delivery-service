package org.example.deliveryservice.configuration.openApiConfig;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {


    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery Service")
                        .description("Foods Delivery")
                        .version("10")
                        .contact(new Contact()
                                .name("Oybek")
                                .email("oybekolimov001@gmail.com")
                                .url("https://github.com/oybek-olimov/food-delivery-service"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .termsOfService("http://swagger.io/terms/"))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080").description("Production Server"),
                        new Server()
                                .url("http://localhost:9090").description("Test Server")
                )).addSecurityItem(new SecurityRequirement().addList( "bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                );

    }
}
