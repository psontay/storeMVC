package com.sontaypham.storemvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    @GetMapping("/signin")
    public String signinPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Wrong username or password!");
        }
        if (logout != null) {
            model.addAttribute("msg", "Logout success!");
        }

        return "auth/signin";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register"; // trả về templates/auth/register.html
    }
}
