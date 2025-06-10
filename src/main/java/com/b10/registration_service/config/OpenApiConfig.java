package com.b10.registration_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI registrationServiceOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local server"),
                        new Server().url("https://api.yourcompany.com").description("Production server")
                ))
                .info(new Info()
                        .title("Registration Service API")
                        .description("""
                            REST API для управления процессами регистрации бизнесов.
                            
                            Сервис реализует state machine для отслеживания этапов регистрации:
                            от подписания налоговых документов до получения данных ASAN Finance.
                            
                            Основные возможности:
                            - Запуск новых процессов регистрации
                            - Отслеживание текущего состояния процессов
                            - Управление переходами между состояниями
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@yourcompany.com")
                                .url("https://yourcompany.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
