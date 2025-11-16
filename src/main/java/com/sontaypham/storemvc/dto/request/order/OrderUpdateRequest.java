package com.sontaypham.storemvc.dto.request.order;

import com.sontaypham.storemvc.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
  String shippingAddress;
  OrderStatus orderStatus;
}
