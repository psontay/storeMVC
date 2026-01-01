package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.response.dashboard.DashboardResponse;
import com.sontaypham.storemvc.service.DashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final DashboardService dashboardService;

  @GetMapping("/dashboard")
  public String adminDashboard(Model model) {
      DashboardResponse data = dashboardService.getDashboard();
      model.addAttribute("data", data);
    return "admin/dashboard";
  }
}
