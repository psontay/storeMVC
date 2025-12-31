package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.service.CategoryService;
import com.sontaypham.storemvc.service.ProductService;
import com.sontaypham.storemvc.service.SupplierService;
import jakarta.validation.Valid;
import java.util.UUID;
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
      @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      Model model) {

    Page<ProductResponse> page =
        (name != null && !name.isBlank())
            ? productService.findByNameContaining(name.trim(), pageable)
            : productService.findAll(pageable);

    model.addAttribute("page", page);
    model.addAttribute("currentName", name);
    model.addAttribute("categories", categoryService.findAll());
    model.addAttribute("suppliers", supplierService.findAll());

    return "admin/product-management";
  }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            RedirectAttributes ra) {

        Page<ProductResponse> page =
                (name != null && !name.isBlank())
                        ? productService.findByNameContaining(name.trim(), pageable)
                        : productService.findAll(pageable);

        model.addAttribute("page", page);
        model.addAttribute("currentName", name);

        try {
            ProductResponse product = productService.findById(id);
            model.addAttribute("editProduct", product);

            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("suppliers", supplierService.findAll());

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Product not found!");
            return "redirect:/admin/products";
        }
        return "admin/product-management";
    }

  @PostMapping("/create")
  public String create(
      @Valid @ModelAttribute ProductCreationRequest request,
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
  public String update(
      @PathVariable UUID id,
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
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            Model model) {

        Page<ProductResponse> page =
                productService.findByProductNameOrSupplierNameContainingIgnoreCase(
                        keyword != null ? keyword : "",
                        keyword != null ? keyword : "",
                        pageable);

        model.addAttribute("page", page);
        model.addAttribute("currentName", keyword);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("suppliers", supplierService.findAll());
        return "admin/product-management";
    }
    @GetMapping("/trash")
    public String trash(
            @PageableDefault(size = 12, sort = "deleted_at", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<ProductResponse> page = productService.getTrash(pageable);
        model.addAttribute("page", page);
        return "admin/product-trash";
    }

    @PostMapping("/restore/{id}")
    public String restore(@PathVariable UUID id, RedirectAttributes ra) {
        try {
            productService.restore(id);
            ra.addFlashAttribute("success", "Product restored successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error restoring product.");
        }
        return "redirect:/admin/products/trash";
    }

    @PostMapping("/hard-delete/{id}")
    public String hardDelete(@PathVariable UUID id, RedirectAttributes ra) {
        try {
            productService.hardDelete(id);
            ra.addFlashAttribute("success", "Product deleted forever!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error deleting product.");
        }
        return "redirect:/admin/products/trash";
    }

}
