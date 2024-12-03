package com.checkhouse.core.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@SecurityScheme(
        type= SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        name = "Authorization",
        description = "Auth token"
)
@Configuration
public class SwaggerConfig {

    @Value("${springdoc.title}")
    private String title;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(
                        new SecurityRequirement().addList("Authorization")
                );
    }

    private Info apiInfo() {
        return new Info()
                .title(title + " API DOC")
                .description("For " + title + " service api document")
                .version("1.0.0");
}
    }
