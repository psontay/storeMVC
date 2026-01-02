package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.response.cart.CartItemResponse;
import com.sontaypham.storemvc.dto.response.cart.CartResponse;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import com.sontaypham.storemvc.dto.response.user.UserResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.Cart;
import com.sontaypham.storemvc.model.CartItem;
import com.sontaypham.storemvc.model.CustomUserDetails;
import com.sontaypham.storemvc.service.CartService;
import com.sontaypham.storemvc.service.OrderService;
import com.sontaypham.storemvc.service.UserService;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {
  OrderService orderService;
  CartService cartService;
  UserService userService;

  @GetMapping("/checkout")
  public String checkout(
      @RequestParam(name = "selectedCartItemIds", required = false) List<UUID> selectedCartItemIds,
      Model model) {
    UUID userId = SecurityUtilStatic.getUserId();
    UserResponse user =
        userService.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    CartResponse cart = cartService.getCartByUserId(userId);

    if (cart.getItems().isEmpty()) {
      return "redirect:/cart";
    }

    if (selectedCartItemIds != null && !selectedCartItemIds.isEmpty()) {
      List<CartItemResponse> filteredItems =
          cart.getItems().stream()
              .filter(item -> selectedCartItemIds.contains(item.getId()))
              .toList();

      cart.setItems(filteredItems);

      BigDecimal newTotal =
          filteredItems.stream()
              .map(CartItemResponse::getSubtotal)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      cart.setTotalPrice(newTotal);
    } else {
      return "redirect:/cart";
    }

    OrderCreationRequest request = new OrderCreationRequest();
    request.setSelectedCartItemIds(selectedCartItemIds);
    request.setShippingAddress(user.getAddress());

    model.addAttribute("cart", cart);
    model.addAttribute("request", request);

    return "order/checkout";
  }

  @PostMapping("/create")
  public String createOrder(
      @ModelAttribute OrderCreationRequest request, RedirectAttributes redirectAttributes) {
    request.setUserId(SecurityUtilStatic.getUserId());

    if (request.getSelectedCartItemIds() == null || request.getSelectedCartItemIds().isEmpty()) {
      Cart cart = cartService.getCartEntityByUserId(request.getUserId());
      request.setSelectedCartItemIds(cart.getItems().stream().map(CartItem::getId).toList());
    }

    try {
      OrderResponse order = orderService.createOrder(request);
      redirectAttributes.addFlashAttribute(
          "success", "Create order success! Order ID: " + order.getOrderId());
      return "redirect:/order/success/" + order.getOrderId();
    } catch (ApiException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/cart";
    }
  }

  @GetMapping("/success/{orderId}")
  public String orderSuccess(@PathVariable UUID orderId, Model model) {
    model.addAttribute("orderId", orderId);
    return "order/success";
  }

  @GetMapping("/history")
  public String orderHistory(Model model) {
    List<OrderResponse> orders = orderService.getAllOrdersByUserId(SecurityUtilStatic.getUserId());
    model.addAttribute("orders", orders);
    return "order/history";
  }

  @GetMapping("/{orderId}")
  public String viewOrderDetail(
      @PathVariable UUID orderId,
      Model model) {
    UUID userId = SecurityUtilStatic.getUserId();
    OrderResponse order = orderService.getOrderDetailsByIdAndUserId(orderId, userId);
    model.addAttribute("order", order);

    return "order/details";
  }
}
