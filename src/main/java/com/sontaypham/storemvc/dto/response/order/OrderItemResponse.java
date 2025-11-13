package com.sontaypham.storemvc.dto.response.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    UUID productId;
    String productName;
    int quantity;
    BigDecimal unitPrice;
}
