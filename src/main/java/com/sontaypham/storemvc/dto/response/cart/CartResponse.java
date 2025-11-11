package com.sontaypham.storemvc.dto.response.cart;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    UUID id;
    UUID userId;
    String username;
    List<CartItemResponse> items;
    BigDecimal totalPrice;
}
