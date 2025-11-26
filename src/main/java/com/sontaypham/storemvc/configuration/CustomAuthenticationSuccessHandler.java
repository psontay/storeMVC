package com.sontaypham.storemvc.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    var authorities = authentication.getAuthorities();
    log.info("login success user : " + authentication.getName());
    log.info("with role : " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    String redirectUrl = "/";
    if (authorities.stream().anyMatch(a -> a.getAuthority().contains("ADMIN"))) {
      redirectUrl = "/admin/dashboard";
    } else if (authorities.stream().anyMatch(a -> a.getAuthority().contains("USER"))) {
      redirectUrl = "/";
    }
    response.sendRedirect(redirectUrl);
  }
}
