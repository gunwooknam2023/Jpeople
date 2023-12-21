package com.gunwook.jpeople.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() { //경로 설정, 여러개 만들 수 있음
        return GroupedOpenApi.builder()
                .group("myApp")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI MyOpenApi() {
        return new OpenAPI()
                .info(new Info().title("SongGPT")
                        .description("JPEOPLE 프로젝트의 API 명세서입니다.")
                        .version("v2.1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
