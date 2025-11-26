package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.order.OrderCreationRequest;
import com.sontaypham.storemvc.dto.response.order.OrderResponse;
import com.sontaypham.storemvc.model.Order;
import com.sontaypham.storemvc.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
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
    OrderResponse fromEntityToResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}
