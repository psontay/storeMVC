package com.sontaypham.storemvc.dto.response.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    UUID productId;
    String productName;
    String productImageUrl;
    int quantity;
    BigDecimal unitPrice;
    BigDecimal subtotal;
}
