package com.example.bookadmin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookAdminOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("图书后台管理系统 API 文档")
                        .description("基于 Spring Boot 3 + Vue3 + MySQL 的图书后台管理系统接口文档")
                        .version("v1.0"));
    }
}
