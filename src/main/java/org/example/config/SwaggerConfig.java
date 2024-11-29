package org.example.config;



import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.media.Schema;

import java.util.List;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sport Supply API")
                        .version("1.0")
                        .description("This is the API documentation for sports nutrition store."))
                .servers(List.of(new Server().url("http://localhost:8080")))
                .components(new Components()
                        .addSchemas("FileUpload", new Schema<>()
                                .type("object")
                                .addProperties("file", new Schema<>()
                                        .type("string")
                                        .format("binary"))) // Определение файла
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }
}


