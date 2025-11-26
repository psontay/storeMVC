package com.sontaypham.storemvc.controller;

import com.sontaypham.storemvc.dto.request.cart.CartItemRequest;
import com.sontaypham.storemvc.service.CartService;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Integer>> addToCart(@RequestBody CartItemRequest request) {
        cartService.addToCart(request.getProductId(), request.getQuantity());
        int totalItems = cartService.getCartByUserId(SecurityUtilStatic.getUserId())
                                    .getItems()
                                    .size();
        return ResponseEntity.ok(Map.of("totalItems", totalItems));
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody CartItemRequest request) {
        cartService.updateQuantity(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable UUID id) {
        cartService.removeFromCart(id);
        return ResponseEntity.ok().build();
    }
}
