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
        cart.recalcTotal();
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
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.recalcSubtotal();
        }else {
            cart.getItems().add(CartItem.builder().cart(cart).product(product).quantity(quantity).unitPrice(product.getPrice()).build());
        }
        cart.recalcTotal();
        cartRepository.save(cart);
    }

    @Override
    public void updateQuantity( UUID productId, int quantity) {
        User user = getCurrentUser();
        Cart cart = getCurrentUserCart();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        Optional<CartItem> existingItem = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(product.getId())).findFirst();
        if ( existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(quantity);
            item.recalcSubtotal();
            cart.recalcTotal();
            cartRepository.save(cart);
        }else {
            throw new ApiException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART);
        }
        cart.recalcTotal();
        cartRepository.save(cart);
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


    private User getCurrentUser() {
        return userRepository.findById(SecurityUtilStatic.getUserId()).orElseThrow(() -> new ApiException(ErrorCode.UNAUTHENTICATED));
    }
    private Cart getCurrentUserCart() {
        return cartRepository.findByUserId(SecurityUtilStatic.getUserId()).orElseGet(() -> cartRepository.save(Cart.builder().user(getCurrentUser()).build()));
    }
}
