package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import com.sontaypham.storemvc.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  @Mapping(target = "user.id", source = "userId")
  Order fromCreationToEntity(OrderCreationRequest orderCreationRequest);

  void updateEntityFromRequest(
      OrderCreationRequest orderCreationRequest, @MappingTarget Order order);

  @Mapping(target = "orderId", source = "id")
  @Mapping(target = "userEmail", source = "user.email")
  @Mapping(target = "orderItems", source = "orderItems")
  OrderResponse fromEntityToResponse(Order order);
}
