package com.sontaypham.storemvc.dto.request.order;

import com.sontaypham.storemvc.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateStatusRequest {
  @NotNull(message = "order must not be null")
  OrderStatus orderStatus;
}
