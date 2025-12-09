package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.response.category.CategoryResponse;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.model.Product;
import com.sontaypham.storemvc.service.CartService;
import com.sontaypham.storemvc.service.CategoryService;
import com.sontaypham.storemvc.service.ProductService;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CartService cartService;

    @GetMapping
    public String home(@PageableDefault(size = 16) Pageable pageable, Model model) {
        model.addAttribute("page", productService.findAll(pageable));
        return "home/home";
    }

    @GetMapping("/category/{categoryId}")
    public String viewCategory(
            @PathVariable UUID categoryId,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        model.addAttribute("page", productService.findByCategoryId(categoryId, pageable));
        return "home/home";
    }

    @ModelAttribute("allCategories")
    public List<CategoryResponse> populateCategories() {
        return categoryService.findAll();
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
    public String details( @PathVariable UUID id , Model model , RedirectAttributes ra) {
        try {
            ProductResponse product = productService.findById(id);
            model.addAttribute("product", product);
        }catch (Exception e) {
            ra.addFlashAttribute("error", "Cannot find product because of an error!");
            return "/home/home";
        }
        return "/product/details";
    }
}
