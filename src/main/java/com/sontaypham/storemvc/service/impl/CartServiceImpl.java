package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.response.cart.CartResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.mapper.CartMapper;
import com.sontaypham.storemvc.model.*;
import com.sontaypham.storemvc.repository.CartRepository;
import com.sontaypham.storemvc.repository.ProductRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import com.sontaypham.storemvc.service.CartService;
import com.sontaypham.storemvc.util.SecurityUtilBean;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    CartMapper cartMapper;

    @Override
    public CartResponse getCartByUserId() {
        User user = getCurrentUser();
        Cart cart = getCurrentUserCart();
        Set<CartItem> cartItems = cart.getItems();
        BigDecimal totalPrice = cart.getTotalPrice();
        cart.setTotalPrice(totalPrice);
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return cartMapper.fromEntityToResponse(cart);
    }

    @Override
    @Transactional
    public void addToCart(UUID productId, int quantity) {
        User user = getCurrentUser();
        Cart cart = getCurrentUserCart();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        Optional<CartItem> existingItem = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(product.getId())).findFirst();
        if ( existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        }else {
            cart.getItems().add(CartItem.builder().cart(cart).product(product).quantity(quantity).subtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity))).build());
        }
        cart.setTotalPrice(calculateTotalPrice(cart.getItems()));
        cart = cartRepository.save(cart);
    }

    @Override
    public void updateQuantity( UUID productId, int quantity) {
        User user = getCurrentUser();
        Cart cart = getCurrentUserCart();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        Optional<CartItem> existingItem = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(product.getId())).findFirst();
        if ( existingItem.isPresent()) existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
    }

    @Override
    public void removeFromCart( UUID productId) {
        Cart cart = getCurrentUserCart();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        CartItem itemRemove = cart.getItems().stream().filter( i -> i.getProduct().getId().equals(product.getId())).findFirst().orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        cart.removeItem(itemRemove);
        cartRepository.save(cart);
    }

    @Override
    public void clearCart() {
        Cart cart = getCurrentUserCart();
        cart.getItems().clear();
        cart.recalcTotal();
        cartRepository.save(cart);
    }

    private BigDecimal calculateTotalPrice(Set<CartItem> items) {
        return items.stream().map( item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private User getCurrentUser() {
        return userRepository.findById(SecurityUtilStatic.getUserId()).orElseThrow(() -> new ApiException(ErrorCode.UNAUTHENTICATED));
    }
    private Cart getCurrentUserCart() {
        return cartRepository.findByUserId(SecurityUtilStatic.getUserId()).orElseGet(() -> cartRepository.save(Cart.builder().user(getCurrentUser()).build()));
    }
}
