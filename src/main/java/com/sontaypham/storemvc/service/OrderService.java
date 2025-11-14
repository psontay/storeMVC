package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderCreationRequest orderCreationRequest);
}
