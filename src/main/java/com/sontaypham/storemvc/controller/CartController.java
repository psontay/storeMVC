package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.response.cart.CartResponse;
import com.sontaypham.storemvc.service.CartService;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        try {
            CartResponse cart = cartService.getCartByUserId(SecurityUtilStatic.getUserId());
            model.addAttribute("cart", cart);
            model.addAttribute("cartItemCount", cart.getItems().size());
        } catch (Exception e) {
            model.addAttribute("cart", CartResponse.builder().items(List.of()).totalPrice(BigDecimal.ZERO).build());
            model.addAttribute("cartItemCount", 0);
        }
        return "cart/cart";
    }
    @ModelAttribute("cartItemCount")
    public int populateCartItemCount() {
        try {
            UUID userId = SecurityUtilStatic.getUserId();
            if (userId != null) {
                return cartService.getCartByUserId(userId).getItems().size();
            }
        } catch (Exception e) {
        }
        return 0;
    }
}
