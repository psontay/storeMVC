package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.supplier.SupplierCreationRequest;
import com.sontaypham.storemvc.dto.request.supplier.SupplierUpdateRequest;
import com.sontaypham.storemvc.dto.response.supplier.SupplierResponse;
import com.sontaypham.storemvc.service.SupplierService;
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
@RequestMapping("/admin/suppliers")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SupplierController {

  private final SupplierService supplierService;

  @GetMapping
  public String list(
      @RequestParam(required = false) String name,
      @PageableDefault(size = 10, sort = "name") Pageable pageable,
      Model model) {

    Page<SupplierResponse> page =
        name != null && !name.isBlank()
            ? supplierService.findByNameContaining(name.trim(), pageable)
            : supplierService.findAll(pageable);

    model.addAttribute("page", page);
    return "admin/supplier-management";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable UUID id, Model model, RedirectAttributes ra) {
    try {
      model.addAttribute("editSupplier", supplierService.getSupplierById(id));

      if (!model.containsAttribute("page")) {
        Page<SupplierResponse> page = supplierService.findAll(Pageable.unpaged());
        model.addAttribute("page", page);
      }

    } catch (Exception e) {
      ra.addFlashAttribute("error", "Không tìm thấy nhà cung cấp!");
      return "redirect:/admin/suppliers";
    }
    return "admin/supplier-management";
  }

  @PostMapping("/create")
  public String create(
      @Valid @ModelAttribute SupplierCreationRequest request,
      BindingResult result,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      ra.addFlashAttribute("error", "Vui lòng kiểm tra lại dữ liệu!");
    } else {
      try {
        supplierService.createSupplier(request);
        ra.addFlashAttribute("success", "Thêm nhà cung cấp thành công!");
      } catch (Exception e) {
        ra.addFlashAttribute("error", e.getMessage());
      }
    }
    return "redirect:/admin/suppliers";
  }

  @PostMapping("/edit/{id}")
  public String update(
      @PathVariable UUID id,
      @Valid @ModelAttribute SupplierUpdateRequest request,
      BindingResult result,
      RedirectAttributes ra) {
    if (result.hasErrors()) {
      return "redirect:/admin/suppliers/edit/" + id;
    }
    try {
      supplierService.updateSupplier(id, request);
      ra.addFlashAttribute("success", "Cập nhật thành công!");
    } catch (Exception e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/suppliers";
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable UUID id, RedirectAttributes ra) {
    try {
      supplierService.deleteSupplierById(id);
      ra.addFlashAttribute("success", "Xóa thành công!");
    } catch (Exception e) {
      ra.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
    }
    return "redirect:/admin/suppliers";
  }

  @GetMapping("/search")
  public String search(
      @RequestParam(required = false) String keyword,
      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      Model model) {
    Page<SupplierResponse> page;
    if (keyword == null || keyword.trim().isEmpty()) {
      page = supplierService.findAll(pageable);
    } else {
      try {
        SupplierResponse supplier = supplierService.getSupplierByName(keyword);

        page = new PageImpl<>(List.of(supplier), pageable, 1);
      } catch (Exception e) {
        page = Page.empty(pageable);
      }
    }

    model.addAttribute("page", page);
    model.addAttribute("currentName", keyword);

    return "admin/supplier-management";
  }
}
