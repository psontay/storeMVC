package com.sontaypham.storemvc.dto.request.product;

import com.sontaypham.storemvc.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {
    String name;
    String description;
    int age;
    String origin;
    String imageUrl;
    int stockQuantity;
    BigDecimal price;
    BigDecimal originalPrice;
    BigDecimal discountedPrice;
    int discountPercent;
    ProductStatus status;
    UUID categoryId;
    UUID supplierId;
}
