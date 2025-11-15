package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.request.order.OrderUpdateRequest;
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
import com.sontaypham.storemvc.util.SecurityUtilBean;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Set<CartItem> selectedItems = cart.getItems().stream().filter( i -> orderCreationRequest.getSelectedCartItemIds().contains(i.getId())).collect(
                Collectors.toSet());
        if ( selectedItems.isEmpty()) {
            throw new ApiException(ErrorCode.CART_IS_EMPTY);
        }
        Order order = Order.builder().user(user).orderStatus(OrderStatus.PENDING)
                .shippingAddress(orderCreationRequest.getShippingAddress()).build();
        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for ( CartItem cartItem : selectedItems) {
            OrderItem orderItem = OrderItem.builder().product(cartItem.getProduct()).order(order).quantity(cartItem.getQuantity()).unitPrice(cartItem.getUnitPrice()).build();
            orderItems.add(orderItem);
            totalPrice = totalPrice.add(cartItem.getUnitPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        Order saved = orderRepository.save(order);
        cart.getItems().removeAll(selectedItems);
        cart.recalcTotal();
        cartRepository.save(cart);
        return orderMapper.fromEntityToResponse(saved);
    }

    @Override
    public OrderResponse updateOrder(UUID id , OrderUpdateRequest orderUpdateRequest) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
        if ( order.getShippingAddress() != null) order.setShippingAddress(orderUpdateRequest.getShippingAddress());
        if ( order.getOrderStatus() != null) order.setOrderStatus(orderUpdateRequest.getOrderStatus());
        orderRepository.save(order);
        return orderMapper.fromEntityToResponse(order);
    }

    @Override
    public void deleteOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
        if ( !order.getUser().getId().equals(SecurityUtilStatic.getUserId())) throw new ApiException(ErrorCode.ACCESS_DENIED);
        orderRepository.delete(order);
    }

}
