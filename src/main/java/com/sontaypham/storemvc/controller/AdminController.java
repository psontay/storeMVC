package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final RevenueService revenueService;
    @GetMapping("/dashboard")
    public String adminDashboard(Model model){
        return "admin/dashboard";
    }
    @GetMapping("/revenueToday")
    public Map<String , Object> revenueToday(){
        BigDecimal todayRevenue =revenueService.getTodayRevenue();
        return Map.of("todayRevenue",todayRevenue);
    }
}
