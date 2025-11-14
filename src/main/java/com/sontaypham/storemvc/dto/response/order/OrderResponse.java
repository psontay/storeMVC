package com.sontaypham.storemvc.dto.response.order;

import com.sontaypham.storemvc.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    UUID orderId;
    String userEmail;
    OrderStatus orderStatus;
    String shippingAddress;
    BigDecimal totalPrice;
    LocalDateTime orderDate;
    Set<OrderItemResponse> orderItems;
}
