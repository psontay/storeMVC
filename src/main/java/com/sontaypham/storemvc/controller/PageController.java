package com.sontaypham.storemvc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @Value("${SPRING_ADMIN_EMAIL_SERVICE}")
    String ADMIN_MAIL;
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
}
