package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.user.ChangePasswordRequest;
import com.sontaypham.storemvc.dto.response.category.CategoryResponse;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.dto.response.supplier.SupplierResponse;
import com.sontaypham.storemvc.dto.response.user.ProfileResponse;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.service.*;
import com.sontaypham.storemvc.util.SecurityUtilStatic;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {

  ProductService productService;
  CategoryService categoryService;
  CartService cartService;
  ProfileService profileService;
  SupplierService supplierService;
  UserService userService;

    @GetMapping
    public String home(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<ProductResponse> page = productService.searchProducts(q, minPrice, maxPrice, pageable);

        model.addAttribute("page", page);

        model.addAttribute("searchQuery", q);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        model.addAttribute("currentCategoryId", null);

        return "home/home";
    }

  @GetMapping("/category/{categoryId}")
  public String viewCategory(
      @PathVariable UUID categoryId,
      @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      Model model) {

    model.addAttribute("page", productService.findByCategoryId(categoryId, pageable));
    return "home/home";
  }

  @ModelAttribute("allCategories")
  public List<CategoryResponse> populateCategories() {
    return categoryService.findAll();
  }

  @ModelAttribute("allSuppliers")
  public List<SupplierResponse> populateSuppliers() {
    return supplierService.findAll();
  }

  @ModelAttribute("cartItemCount")
  public int populateCartItemCount() {
    try {
      UUID userId = SecurityUtilStatic.getUserId();
      return userId != null ? cartService.getCartByUserId(userId).getItems().size() : 0;
    } catch (Exception e) {
      return 0;
    }
  }

  @ModelAttribute("currentCategoryId")
  public UUID currentCategoryId(@PathVariable(required = false) UUID categoryId) {
    return categoryId;
  }

  @GetMapping("product/{id}")
  public String details(@PathVariable UUID id, Model model, RedirectAttributes ra) {
    try {
      ProductResponse product = productService.findById(id);
      model.addAttribute("product", product);
    } catch (Exception e) {
      ra.addFlashAttribute("error", "Cannot find product because of an error!");
      return "/home/home";
    }
    return "/product/details";
  }

  @GetMapping("profile")
  public String profile(Model model, RedirectAttributes ra) {
    try {
      ProfileResponse profileResponse = profileService.getCurrentProfile();
      model.addAttribute("profileResponse", profileResponse);
    } catch (Exception e) {
      ra.addFlashAttribute("error", "Cannot access this profile because of an error!");
      return "/home/home";
    }
    return "/user/profile";
  }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute ChangePasswordRequest request, RedirectAttributes ra) {
        try {
            UUID userId = SecurityUtilStatic.getUserId();
            userService.changePassword(userId, request);
            ra.addFlashAttribute("success", "Change password successfully!");
        } catch (ApiException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "An error occurred while changing password.");
        }
        return "redirect:/profile";
    }

  @GetMapping("/search")
  public String searchProducts(
      @RequestParam(required = false) String q,
      @PageableDefault(size = 12) Pageable pageable,
      Model model) {

    if (q == null || q.trim().isEmpty()) {
      return "redirect:/";
    }

    Page<ProductResponse> page = productService.findByNameContainingIgnoreCase(q.trim(), pageable);
    model.addAttribute("page", page);
    model.addAttribute("searchQuery", q.trim());
    model.addAttribute("currentCategoryId", null);

    return "home/home";
  }
}
