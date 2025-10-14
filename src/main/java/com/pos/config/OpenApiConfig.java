package com.pos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("POS (Point of Sale) Application")
                        .version("1.0.0")
                        .description("Api documentation for this system")
                        .contact(new Contact()
                                .name("Orion")
                                .email("nnmm5122004@gmail.com"))
                );
    }
}
