package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.category.CategoryCreationRequest;
import com.sontaypham.storemvc.dto.request.category.CategoryUpdateRequest;
import com.sontaypham.storemvc.dto.response.category.CategoryResponse;
import com.sontaypham.storemvc.service.CategoryService;
import com.sontaypham.storemvc.service.ProductService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;
  private ProductService productService;

  @GetMapping({"", "/edit/{id}"})
  public String listOrEdit(
      @PathVariable(required = false) UUID id,
      @RequestParam(required = false) String name,
      @PageableDefault(size = 10, sort = "name") Pageable pageable,
      Model model) {

    // Luôn có page
    Page<CategoryResponse> page =
        name != null && !name.isBlank()
            ? categoryService.findByNameContainingIgnoreCase(name.trim(), pageable)
            : categoryService.findAll(pageable);
    model.addAttribute("page", page);

    // Nếu có id → mở form sửa
    if (id != null) {
      try {
        model.addAttribute("editCategory", categoryService.findById(id));
      } catch (Exception e) {
        model.addAttribute("error", "Không tìm thấy danh mục!");
      }
    }

    return "admin/category-management";
  }

  @PostMapping("/create")
  public String create(
      @Valid @ModelAttribute CategoryCreationRequest request,
      BindingResult result,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      ra.addFlashAttribute("error", "Vui lòng kiểm tra lại!");
    } else {
      try {
        categoryService.createCategory(request);
        ra.addFlashAttribute("success", "Thêm danh mục thành công!");
      } catch (Exception e) {
        ra.addFlashAttribute("error", e.getMessage());
      }
    }
    return "redirect:/admin/categories";
  }

  @PostMapping("/edit/{id}")
  public String update(
      @PathVariable UUID id,
      @Valid @ModelAttribute CategoryUpdateRequest request,
      BindingResult result,
      RedirectAttributes ra) {
    request.setId(id);
    if (result.hasErrors()) {
      return "redirect:/admin/categories/edit/" + id;
    }
    try {
      categoryService.updateCategory(request);
      ra.addFlashAttribute("success", "Cập nhật thành công!");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/categories";
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable UUID id, RedirectAttributes ra) {
    try {
      categoryService.detele(id);
      ra.addFlashAttribute("success", "Xóa thành công!");
    } catch (Exception e) {
      ra.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
    }
    return "redirect:/admin/categories";
  }
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<CategoryResponse> page;

        if (keyword == null || keyword.trim().isEmpty()) {
            page = categoryService.findAll(pageable);
        } else {
            try {
                CategoryResponse category = categoryService.findByName(keyword);

                page = new PageImpl<>(List.of(category), pageable, 1);
            } catch (Exception e) {
                page = Page.empty(pageable);
            }
        }

        model.addAttribute("page", page);
        model.addAttribute("currentName", keyword);

        return "admin/category-management";
    }
}
