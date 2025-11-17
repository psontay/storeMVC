package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.user.UserRegisterRequest;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    @GetMapping("/signin")
    public String signinPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("loginError", "Wrong username or password!");
        }
        if (logout != null) {
            model.addAttribute("msg", "Logout success!");
        }

        return "auth/auth";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterRequest request, Model model) {
        try {
            userService.registerUser(request);
            model.addAttribute("success", "Register successfully! Please sign in.");
            return "auth/auth";
        } catch (ApiException ex) {
            model.addAttribute("registerError", ex.getMessage());
            return "auth/auth";
        }
    }
}
