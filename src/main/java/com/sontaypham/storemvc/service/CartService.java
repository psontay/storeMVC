package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.response.cart.CartResponse;

import java.util.UUID;

public interface CartService {
    CartResponse getCartByUserId (UUID userId);
    void addToCart(UUID userId, UUID productId, int quantity);
    void updateQuantity(UUID userId, UUID productId, int quantity);
    void removeFromCart(UUID userId, UUID productId);
    void clearCart(UUID userId);
}
