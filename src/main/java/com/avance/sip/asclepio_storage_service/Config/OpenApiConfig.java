package com.avance.sip.asclepio_storage_service.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI catalogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Asclepio Catalog Service API")
                        .description("API responsável pelo catálogo central de produtos, categorias e variações.")
                        .version("1.0.0"));
    }
}