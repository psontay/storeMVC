package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${SPRING_INIT_ADMIN_EMAIL}")
    String host;
    final JavaMailSender javaMailSender;
    final TemplateEngine templateEngine;

    @Override
    public void sendTextEmail(Object object) {

    }

    @Override
    public void sendTemplateEmail(Object object) {

    }
}
