package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.response.order.OrderItemResponse;
import com.sontaypham.storemvc.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper( componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemResponse fromEntityToResponse(OrderItem orderItem);
    Set<OrderItemResponse> toResponseSet(Set<OrderItem> orderItems);
}
