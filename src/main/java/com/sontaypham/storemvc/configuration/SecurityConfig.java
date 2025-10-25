package com.sontaypham.storemvc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] publicEndpoints = {
      "/" ,
      "/auth/login",
            "/auth/register",
            "/auth/logout",
    };
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(HttpSecurity http , AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

    }
}
