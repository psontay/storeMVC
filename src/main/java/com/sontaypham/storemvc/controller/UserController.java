package com.sontaypham.storemvc.controller;


import com.sontaypham.storemvc.dto.response.user.UserResponse;
import com.sontaypham.storemvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public String listUsers(@RequestParam( value = "page" , defaultValue = "0") int page, @RequestParam(value = "size" , defaultValue = "10") int size, Model model) {
        Pageable pageable = PageRequest.of(page , size , (Sort.by("username").ascending()));
        Page<UserResponse> userPage = userService.findAll(pageable);
        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage" , page);
        model.addAttribute("pageSize" , size);
        return "admin/user-management";
    }
}
