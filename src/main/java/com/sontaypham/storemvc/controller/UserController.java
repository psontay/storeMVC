package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.user.UserCreationRequest;
import com.sontaypham.storemvc.dto.request.user.UserUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.dto.response.user.UserResponse;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.mapper.PermissionMapper;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.repository.PermissionRepository;
import com.sontaypham.storemvc.repository.RoleRepository;
import com.sontaypham.storemvc.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final PermissionRepository permissionRepository;
  private final PermissionMapper permissionMapper;
  private final RoleRepository roleRepository;

  // list user
  @GetMapping
  public String listUsers(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      Model model) {
    Pageable pageable = PageRequest.of(page, size, (Sort.by("username").ascending()));
    Page<UserResponse> userPage = userService.findAll(pageable);
    model.addAttribute("userPage", userPage);
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", size);
    List<String> permissions =
        permissionRepository.findAll().stream().map(p -> p.getName()).toList();
    model.addAttribute("permissions", permissions);
    return "admin/user-management";
  }

  // create user
  @PostMapping("/create")
  public String createUser(
      @ModelAttribute UserCreationRequest request, RedirectAttributes redirectAttributes) {
    try {
      userService.createUser(request);
      redirectAttributes.addFlashAttribute("success", "Create user success");
    } catch (ApiException e) {
      redirectAttributes.addFlashAttribute(
          "error", e.getErrorCode() != null ? e.getErrorCode().getMessage() : e.getMessage());
    }
    return "redirect:/admin/users";
  }

  @GetMapping("/{username}/edit")
  public String editUser(@PathVariable String username, Model model) {
    UserResponse user = userService.findByUsername(username);
    model.addAttribute("editUserId", username);
    model.addAttribute("editUser", user);
    if (!model.containsAttribute("userPage")) {
      model.addAttribute("userPage", Page.empty());
      model.addAttribute("currentPage", 0);
      model.addAttribute("pageSize", 10);
    }

    List<String> permissions =
        permissionRepository.findAll().stream().map(p -> p.getName()).toList();
    model.addAttribute("permissions", permissions);

    List<String> roles = roleRepository.findAll().stream().map(r -> r.getName()).toList();
    model.addAttribute("roles", roles);

    return "admin/user-management";
  }

  // Update user
  @PostMapping("/{username}/update")
  public String updateUser(
      @PathVariable String username,
      @ModelAttribute UserUpdateRequest request,
      RedirectAttributes redirectAttributes) {
    try {
      request.setUsername(username);
      userService.updateUser(request);
      redirectAttributes.addFlashAttribute("success", "Update user success");
    } catch (ApiException e) {
      redirectAttributes.addFlashAttribute(
          "error", e.getErrorCode() != null ? e.getErrorCode().getMessage() : e.getMessage());
    }
    return "redirect:/admin/users";
  }

  // Delete user
  @PostMapping("/{username}/delete")
  public String deleteUser(@PathVariable String username, RedirectAttributes redirectAttributes) {
    try {
      userService.deleteByUsername(username);
      redirectAttributes.addFlashAttribute("success", "Delete user success");
    } catch (ApiException e) {
      redirectAttributes.addFlashAttribute(
          "error", e.getErrorCode() != null ? e.getErrorCode().getMessage() : e.getMessage());
    }
    return "redirect:/admin/users";
  }
    @GetMapping("/search")
    public String searchUsers(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        populateUserList(page, size, search, model);
        return "admin/user-management";
    }
    private void populateUserList(int page, int size, String search, Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<UserResponse> userPage;

        if (search != null && !search.isBlank()) {
            userPage = userService.findByUsernameOrEmailContainingIgnoreCase(search.trim(), pageable);
            model.addAttribute("currentSearch", search.trim());
        } else {
            userPage = userService.findAll(pageable);
        }

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        List<String> permissions = permissionRepository.findAll().stream().map(Permission::getName).toList();
        model.addAttribute("permissions", permissions);
    }

}
