package org.example.config;


import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sport Supply API") // Заголовок API
                        .version("1.0") // Версия API
                        .description("This is the API documentation for sports nutrition store.")) // Описание API
                .servers(List.of(new Server().url("http://localhost:8080"))); // URL сервера, на котором работает API
    }
}
