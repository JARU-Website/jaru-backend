package com.web.jaru.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // OpenAPI 3 문서 정의
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JARU API")
                        .version("v1")
                        .description("JARU 웹 서비스 API 명세서입니다.")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com")
                        )
                );
    }
}

