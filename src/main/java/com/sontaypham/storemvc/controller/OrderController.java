package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.response.cart.CartResponse;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.Cart;
import com.sontaypham.storemvc.model.CartItem;
import com.sontaypham.storemvc.service.CartService;
import com.sontaypham.storemvc.service.OrderService;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        UUID userId = SecurityUtilStatic.getUserId();
        CartResponse cart = cartService.getCartByUserId(userId);

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("request", new OrderCreationRequest());
        return "order/checkout";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute OrderCreationRequest request,
                              RedirectAttributes redirectAttributes) {
        request.setUserId(SecurityUtilStatic.getUserId());

        if (request.getSelectedCartItemIds() == null || request.getSelectedCartItemIds().isEmpty()) {
            Cart cart = cartService.getCartEntityByUserId(request.getUserId());
            request.setSelectedCartItemIds(
                    cart.getItems().stream().map(CartItem::getId).toList()
                                          );
        }

        try {
            OrderResponse order = orderService.createOrder(request);
            redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công! Mã đơn: " + order.getOrderId());
            return "redirect:/order/success/" + order.getOrderId();
        } catch (ApiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/order/checkout";
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
}
