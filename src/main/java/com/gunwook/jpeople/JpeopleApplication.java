package com.gunwook.jpeople;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaAuditing
public class JpeopleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpeopleApplication.class, args);
    }

}
