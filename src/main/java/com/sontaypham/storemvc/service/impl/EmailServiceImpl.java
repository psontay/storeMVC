package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.response.email.EmailResponse;
import com.sontaypham.storemvc.model.EmailDetails;
import com.sontaypham.storemvc.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${SPRING_ADMIN_EMAIL_SERVICE}")
    String host;
    final JavaMailSender javaMailSender;
    final TemplateEngine templateEngine;

    @Override
    public EmailResponse sendTextEmail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(host);
            message.setTo(emailDetails.getTo());
            message.setSubject(emailDetails.getSubject());
            message.setText(emailDetails.getMessageBody());
            javaMailSender.send(message);
            return EmailResponse.builder()
                                .success(true)
                                .message("Email sent successfully to " + emailDetails.getTo())
                                .build();
        }catch (Exception e){
            log.error(e.getMessage());
            return EmailResponse.builder().message("Failed to sending email").success(false).build();
        }
        return null;
    }

    @Override
    public EmailResponse sendTemplateEmail(EmailDetails emailDetails) {
        return null;
    }
}
