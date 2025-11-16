package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.request.order.OrderUpdateRequest;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(OrderCreationRequest orderCreationRequest);
    OrderResponse updateOrder(UUID id , OrderUpdateRequest orderUpdateRequest);
    void deleteOrder( UUID id);
    List<OrderResponse> getAllOrders( Pageable pageable);
    List<OrderResponse> getAllOrdersByUserId(UUID id);
}
