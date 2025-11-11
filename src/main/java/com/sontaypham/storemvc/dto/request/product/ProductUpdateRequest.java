package com.sontaypham.storemvc.dto.request.product;


import com.sontaypham.storemvc.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
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
    UUID supplierId;
    UUID categoryId;
}
