package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import com.sontaypham.storemvc.model.Order;
import com.sontaypham.storemvc.model.User;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {OrderItemMapper.class})
public interface OrderMapper {

  @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
  Order fromCreationToEntity(OrderCreationRequest request);

  @Named("userFromId")
  default User mapUser(UUID userId) {
    if (userId == null) return null;
    User user = new User();
    user.setId(userId);
    return user;
  }

  @Mapping(target = "orderId", source = "id")
  @Mapping(target = "userEmail", source = "user.email")
  @Mapping(target = "orderItems", ignore = true)
  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "telPhone", source = "user.telPhone")
  OrderResponse fromEntityToResponse(Order order);

  @Mapping(target = "orderId", source = "id")
  @Mapping(target = "userEmail", source = "user.email")
  OrderResponse fromEntityToResponseDetails(Order order);
}
