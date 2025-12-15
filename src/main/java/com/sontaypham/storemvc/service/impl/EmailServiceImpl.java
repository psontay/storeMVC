package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.model.EmailDetails;
import com.sontaypham.storemvc.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

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
    @Async("emailExecutor")
    public void sendTextEmail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(host);
            message.setTo(emailDetails.getTo());
            message.setSubject(emailDetails.getSubject());
            message.setText(emailDetails.getMessageBody());
            javaMailSender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Override
    @Async("emailExecutor")
    public void sendTemplateEmail(EmailDetails emailDetails) {
        try{
            ClassPathResource resource = new ClassPathResource("/static/css/email.css");
            String cssContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            Context context = new Context();
            if (emailDetails.getVariables() != null) {
                emailDetails.getVariables().forEach(context::setVariable);
            }
            context.setVariable("cssContent", cssContent);
            if (emailDetails.getVariables() != null) {
                emailDetails.getVariables().forEach((k, v) -> {
                    log.info("Set Thymeleaf variable: {} = {}", k, v);
                    context.setVariable(k, v);
                });
            }

            String htmlContent = templateEngine.process(emailDetails.getTemplateName(), context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(host);
            helper.setTo(emailDetails.getTo());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Email has been sent successfully to :" + emailDetails.getTo());
        }catch (Exception e){
            log.error("Failed to send template email", e);
        }
    }
}
