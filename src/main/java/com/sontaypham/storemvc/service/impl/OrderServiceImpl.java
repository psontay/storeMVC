package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.enums.OrderStatus;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.mapper.CartMapper;
import com.sontaypham.storemvc.mapper.OrderMapper;
import com.sontaypham.storemvc.model.*;
import com.sontaypham.storemvc.repository.CartRepository;
import com.sontaypham.storemvc.repository.OrderRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import com.sontaypham.storemvc.service.CartService;
import com.sontaypham.storemvc.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE , makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderMapper orderMapper;
    UserRepository userRepository;
    OrderRepository orderRepository;
    CartRepository cartRepository;
    CartService cartService;
    @Override
    public OrderResponse createOrder(OrderCreationRequest orderCreationRequest) {
        User user = userRepository.findById(orderCreationRequest.getUserId()).orElseThrow(() -> new ApiException(
                ErrorCode.USER_NOT_FOUND));
        Cart cart = cartService.getCartEntityByUserId(user.getId());
        if ( cart.getItems().isEmpty() ) {
            throw new ApiException(ErrorCode.CART_IS_EMPTY);
        }
        Order order = orderMapper.fromCreationToEntity(orderCreationRequest);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        BigDecimal total = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem c : cart.getItems()) {
            OrderItem orderItem = OrderItem.builder().order(order).unitPrice(c.getProduct().getPrice()).quantity(c.getQuantity()).product(c.getProduct()).build();
            orderItems.add(orderItem);
            total = total.add( c.getProduct().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())));
        }
        order.setOrderItems(orderItems);
        order.setTotalPrice(total);
        orderRepository.save(order);
        cartService.clearCart(user.getId());
        return orderMapper.fromEntityToResponse(order);
    }
}
