package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.contact.ContactRequest;
import com.sontaypham.storemvc.model.EmailDetails;
import com.sontaypham.storemvc.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PageController {
  @Value("${SPRING_ADMIN_EMAIL_SERVICE}")
  String ADMIN_EMAIL;

  private final EmailService emailService;

  @GetMapping("/privacy")
  public String privacyPage() {
    return "pages/privacy";
  }

  @GetMapping("/terms")
  public String termsPage() {
    return "pages/terms";
  }

  @GetMapping("/support")
  public String supportPage() {
    return "pages/support";
  }

  @PostMapping("/support/send")
  public String sendContactForm(@ModelAttribute ContactRequest request, RedirectAttributes ra) {
    try {
      String content =
          String.format(
              """
                Hey ADMIN, you received new contact details!:
                -----------------------------------
                From: %s
                Email: %s
                -----------------------------------
                Message:
                %s
                """,
              request.getFullName(), request.getEmail(), request.getMessage());

      EmailDetails emailDetails =
          EmailDetails.builder()
              .to(ADMIN_EMAIL)
              .subject("New Contact Message from " + request.getFullName())
              .messageBody(content)
              .build();

      emailService.sendTextEmail(emailDetails);

      ra.addFlashAttribute("success", "Message sent! We will contact you soon.");
    } catch (Exception e) {
      e.printStackTrace();
      ra.addFlashAttribute("error", "Failed to send message. Please try again.");
    }
    return "redirect:/support";
  }
}
