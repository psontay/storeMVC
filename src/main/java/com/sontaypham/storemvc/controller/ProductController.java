package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.enums.ProductStatus;
import com.sontaypham.storemvc.service.CategoryService;
import com.sontaypham.storemvc.service.ProductService;
import com.sontaypham.storemvc.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    @GetMapping
    public String management(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<ProductResponse> page = (name != null && !name.isBlank())
                ? productService.findByNameContaining(name.trim(), pageable)
                : productService.findAll(pageable);

        model.addAttribute("page", page);
        model.addAttribute("currentName", name);

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("suppliers", supplierService.findAll());

        return "admin/product-management";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id, Model model, RedirectAttributes ra) {
        try {
            ProductResponse product = productService.findById(id);
            model.addAttribute("editProduct", product);

            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("suppliers", supplierService.findAll());

            model.addAttribute("currentName", ra.getFlashAttributes().get("currentName"));
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/products";
        }
        return "admin/product-management";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute ProductCreationRequest request,
                         BindingResult result,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("error", "Recheck data pls!");
            return "redirect:/admin/products";
        }
        try {
            productService.createProduct(request);
            ra.addFlashAttribute("success", "Create product successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable UUID id,
                         @Valid @ModelAttribute ProductUpdateRequest request,
                         BindingResult result,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("error", "Recheck data pls!");
            return "redirect:/admin/products/edit/" + id;
        }
        try {
            productService.updateProduct(id, request);
            ra.addFlashAttribute("success", "Update product successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes ra) {
        try {
            productService.delete(id);
            ra.addFlashAttribute("success", "Delete product successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Cannot delete product because of an error!");
        }
        return "redirect:/admin/products";
    }
}