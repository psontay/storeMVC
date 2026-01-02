package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.order.OrderUpdateStatusRequest;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.mapper.OrderMapper;
import com.sontaypham.storemvc.model.Order;
import com.sontaypham.storemvc.repository.OrderRepository;
import com.sontaypham.storemvc.service.OrderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminOrderController {

  OrderService orderService;
  OrderMapper orderMapper;
  OrderRepository orderRepository;
  @PersistenceContext private EntityManager entityManager;

  @GetMapping
  @Transactional(readOnly = true)
  public String showOrderManagement(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "orderDate,desc") String sort,
      Model model) {

    Pageable pageable = PageRequest.of(page, size, parseSort(sort));
    entityManager.clear();
    Page<Order> orderPage = orderRepository.findAll(pageable);
    Page<OrderResponse> responsePage =
        orderPage.map(
            order -> {
              entityManager.detach(order);
              return orderMapper.fromEntityToResponse(order);
            });
    model.addAttribute("page", responsePage);
    model.addAttribute("sort", sort);

    return "admin/order-management";
  }

  @PostMapping("/{orderId}/status")
  public String updateOrderStatus(
      @PathVariable UUID orderId,
      @ModelAttribute OrderUpdateStatusRequest request,
      RedirectAttributes redirectAttributes) {
    log.info("Order id : " + orderId);
    log.info("Order status: " + request.getOrderStatus());

    try {
      Order order =
          orderRepository
              .findById(orderId)
              .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
      orderService.updateOrderStatus(order.getId(), request);
      redirectAttributes.addFlashAttribute("success", "Update order has been saved successfully");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Update order failed: " + e.getMessage());
    }

    return "redirect:/admin/orders";
  }

  @PostMapping("/{orderId}/delete")
  public String deleteOrder(@PathVariable UUID orderId, RedirectAttributes redirectAttributes) {
    try {
      Order order =
          orderRepository
              .findById(orderId)
              .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
      orderRepository.delete(order);
      redirectAttributes.addFlashAttribute("success", "Delete order successfully");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Delete order failed: " + e.getMessage());
    }
    return "redirect:/admin/orders";
  }

  private Sort parseSort(String sort) {
    if (sort == null || sort.isBlank()) {
      return Sort.by(Sort.Direction.DESC, "orderDate");
    }
    String[] parts = sort.split(",");
    String property = parts[0].trim();
    Sort.Direction direction =
        parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim())
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;
    return Sort.by(direction, property);
  }

  @GetMapping("/search")
  public String search(
      @RequestParam(required = false) String keyword,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable,
      Model model) {

    Page<OrderResponse> page;

    if (keyword == null || keyword.trim().isEmpty()) {
      page = Page.empty();
    } else {
      try {
        UUID orderId = UUID.fromString(keyword);

        OrderResponse order = orderService.findById(orderId);

        page = new PageImpl<>(List.of(order), pageable, 1);

      } catch (IllegalArgumentException e) {
        page = Page.empty(pageable);
      } catch (Exception e) {
        page = Page.empty(pageable);
      }
    }

    model.addAttribute("page", page);
    model.addAttribute("currentKeyword", keyword);

    return "admin/order-management";
  }
}
