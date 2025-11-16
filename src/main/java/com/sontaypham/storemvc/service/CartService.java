package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.response.cart.CartResponse;
import com.sontaypham.storemvc.model.Cart;
import java.util.UUID;

public interface CartService {
  CartResponse getCartByUserId(UUID userId);

  Cart getCartEntityByUserId(UUID userId);

  void addToCart(UUID productId, int quantity);

  void updateQuantity(UUID productId, int quantity);

  void removeFromCart(UUID productId);

  void clearCart(UUID userId);
}
