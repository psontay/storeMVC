package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.response.cart.CartResponse;

import java.util.UUID;

public interface CartService {
    CartResponse getCartByUserId ();
    void addToCart(UUID productId, int quantity);
    void updateQuantity(UUID productId, int quantity);
    void removeFromCart( UUID productId);
    void clearCart();
}
