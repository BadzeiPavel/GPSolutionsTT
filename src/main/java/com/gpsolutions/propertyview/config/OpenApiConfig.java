package com.gpsolutions.propertyview.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI propertyViewOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Property View API")
                        .description("RESTful API for working with hotels")
                        .version("1.0.0"));
    }
}
