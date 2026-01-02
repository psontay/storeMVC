package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.request.order.OrderUpdateRequest;
import com.sontaypham.storemvc.dto.request.order.OrderUpdateStatusRequest;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.enums.OrderStatus;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.mapper.OrderMapper;
import com.sontaypham.storemvc.model.*;
import com.sontaypham.storemvc.repository.CartRepository;
import com.sontaypham.storemvc.repository.OrderRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import com.sontaypham.storemvc.service.CartService;
import com.sontaypham.storemvc.service.EmailService;
import com.sontaypham.storemvc.service.OrderService;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
  OrderMapper orderMapper;
  UserRepository userRepository;
  OrderRepository orderRepository;
  CartRepository cartRepository;
  CartService cartService;
  EmailService emailService;

  @Override
  public OrderResponse createOrder(OrderCreationRequest orderCreationRequest) {
    User user =
        userRepository
            .findById(orderCreationRequest.getUserId())
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    Cart cart = cartService.getCartEntityByUserId(user.getId());
    Set<CartItem> selectedItems =
        cart.getItems().stream()
            .filter(i -> orderCreationRequest.getSelectedCartItemIds().contains(i.getId()))
            .collect(Collectors.toSet());
    if (selectedItems.isEmpty()) {
      throw new ApiException(ErrorCode.CART_IS_EMPTY);
    }
    Order order =
        Order.builder()
            .user(user)
            .orderStatus(OrderStatus.PENDING)
            .shippingAddress(orderCreationRequest.getShippingAddress())
            .build();
    Set<OrderItem> orderItems = new HashSet<>();
    BigDecimal totalPrice = BigDecimal.ZERO;
    for (CartItem cartItem : selectedItems) {
      OrderItem orderItem =
          OrderItem.builder()
              .product(cartItem.getProduct())
              .order(order)
              .quantity(cartItem.getQuantity())
              .unitPrice(cartItem.getUnitPrice())
              .build();
      orderItems.add(orderItem);
      totalPrice =
          totalPrice.add(cartItem.getUnitPrice().multiply(new BigDecimal(cartItem.getQuantity())));
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
  @Transactional
  public OrderResponse updateOrder(UUID id, OrderUpdateRequest orderUpdateRequest) {
    Order order =
        orderRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
    if (order.getShippingAddress() != null)
      order.setShippingAddress(orderUpdateRequest.getShippingAddress());
    if (order.getOrderStatus() != null) order.setOrderStatus(orderUpdateRequest.getOrderStatus());
    orderRepository.save(order);
    return orderMapper.fromEntityToResponse(order);
  }

  @Override
  @Transactional
  public void deleteOrder(UUID id) {
    Order order =
        orderRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
    if (!order.getUser().getId().equals(SecurityUtilStatic.getUserId()))
      throw new ApiException(ErrorCode.ACCESS_DENIED);
    orderRepository.delete(order);
  }

  @Override
  public List<OrderResponse> getAllOrders(Pageable pageable) {
    return orderRepository.findAll(pageable).stream()
        .map(orderMapper::fromEntityToResponse)
        .toList();
  }

  @Override
  public List<OrderResponse> getAllOrdersByUserId(UUID id) {
    Set<Order> orders =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND))
            .getOrders();
    return orders.stream().map(orderMapper::fromEntityToResponse).toList();
  }

  @Override
  public OrderResponse getOrderByOrderIdAndUserId(UUID orderId, UUID userId) {
    Order order =
        orderRepository
            .getOrderByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
    return orderMapper.fromEntityToResponse(order);
  }

  @Override
  public OrderResponse getOrderDetailsByIdAndUserId(UUID orderId, UUID userId) {
    Order order =
        orderRepository
            .findOrderDetailByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
    Hibernate.initialize(order.getOrderItems());
    log.info(order.getId().toString());
    return orderMapper.fromEntityToResponseDetails(order);
  }

  @Override
  @Transactional
  public void updateOrderStatus(UUID orderId, OrderUpdateStatusRequest orderUpdateStatusRequest) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
    if (orderUpdateStatusRequest.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
      Set<OrderItem> orderItems = order.getOrderItems();
      orderItems.forEach(
          oi ->
              oi.getProduct()
                  .setStockQuantity(oi.getProduct().getStockQuantity() - oi.getQuantity()));
    }
    order.setOrderStatus(orderUpdateStatusRequest.getOrderStatus());
    Order saved = orderRepository.save(order);
    OrderResponse orderResponse = orderMapper.fromEntityToResponse(saved);
    if (orderResponse.getOrderStatus().equals(OrderStatus.CONFIRMED)) {
      try {
        Map<String, Object> variables = new HashMap<>();
        variables.put("order", orderResponse);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setTo(orderResponse.getUserEmail());
        emailDetails.setSubject("GreenShop - Order Confirmation #" + saved.getId());
        emailDetails.setTemplateName("email/order-confirmation");
        emailDetails.setVariables(variables);

        emailService.sendTemplateEmail(emailDetails);
      } catch (Exception e) {
        log.info("Error while sending confirmation message : " + e.getMessage());
      }
    }
  }

  @Override
  public String getDefaultShippingAddress(UUID userId) {
    return "";
  }

  @Override
  public OrderResponse findById(UUID id) {
    return orderRepository
        .findById(id)
        .map(orderMapper::fromEntityToResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
  }
}
