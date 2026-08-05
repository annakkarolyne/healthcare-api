package com.healthcare.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(com.healthcare.api.config.JwtProperties.class)
public class HealthcareApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareApiApplication.class, args);
    }

}