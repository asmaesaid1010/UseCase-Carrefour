package com.cabinetmedical.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI cabinetMedicalApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API – Cabinet Médical")
                        .description("API REST pour la gestion des médecins, créneaux , patients et rendez-vous.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Support Backend")
                                .email("support@cabinet-medical.com")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Environnement local"),
                        new Server().url("https://api.cabinet-medical.ma").description("Production")
                ));
    }
}