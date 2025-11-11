package com.sontaypham.storemvc.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    UUID id;
    String name;
    String description;
    int age;
    String origin;
    String imageUrl;
    int stockQuantity;

    BigDecimal price;
    BigDecimal originalPrice;
    BigDecimal discountedPrice;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    String status;
    String supplier;
    String category;
}
