package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.auth.UpdatePasswordRequest;
import com.sontaypham.storemvc.dto.request.user.UserRegisterRequest;
import com.sontaypham.storemvc.enums.ForgotPasswordStatus;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  @GetMapping("/forgot-password")
  public String showForgotPasswordPage(Model model) {
      return "auth/forgot-password";
  }
  @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email , RedirectAttributes ra) {
      ForgotPasswordStatus status = userService.forgotPassword(email);
      switch (status) {
          case SUCCESS:
              ra.addFlashAttribute("success", "Email sent successfully!");
              break;
          case EMAIL_NOT_FOUND:
              ra.addFlashAttribute("error", "Email not found!");
              break;
          case SEND_EMAIL_FAILED:
              ra.addFlashAttribute("error", "Send email failed!");
              break;
      }
      return "redirect:/auth/forgot-password";
  }
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
      if (!userService.isValidatePasswordResetToken(token)) { return "redirect:/auth/signin?error=invalid_token"; }

        model.addAttribute("token", token);
        return "auth/reset-password-form";
    }
    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("password") String password,
                                      RedirectAttributes ra) {
        try {
            UpdatePasswordRequest request = UpdatePasswordRequest.builder().token(token).newPassword(password).build();
            userService.resetPassword(request);
            ra.addFlashAttribute("msg", "Password reset successfully! Please login.");
            return "redirect:/auth/signin";
        } catch (ApiException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/reset-password?token=" + token;
        }
    }
}
