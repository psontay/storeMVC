package com.sontaypham.storemvc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationInitConfig {
    @Value("${initAdmin.default.username}")
    String adminUsername;
    @Value("${initAdmin.default.password}")
    String adminPassword;
    @Value("${initAdmin.default.email}")
    String adminEmail;
    @Bean
    ApplicationRunner applicationRunner() {
        return null;
    }
}
