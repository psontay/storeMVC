package com.sontaypham.storemvc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private final String[] publicEndpoints = {
    "/", "/auth/signin", "/auth/register", "/auth/logout", "/css/**" , "/js/**" , "/images/**"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler)
      throws Exception {
//    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(
        authorizeRequests ->
            authorizeRequests
                .requestMatchers(publicEndpoints)
                .permitAll()
                .anyRequest()
                .authenticated());
    http.formLogin(
        form ->
            form.loginPage("/auth/signin")
                .loginProcessingUrl("/auth/signin")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/auth/signin?error=true")
                .permitAll());

    http.logout(
        logout ->
            logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/signin?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
