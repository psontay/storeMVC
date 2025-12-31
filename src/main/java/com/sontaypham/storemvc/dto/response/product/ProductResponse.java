package com.sontaypham.storemvc.dto.response.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
  int discountPercent;


  LocalDateTime createdAt;
  LocalDateTime updatedAt;
  LocalDateTime deletedAt;

  String status;
  UUID supplierId;
  String supplierName;

  Set<String> categoryNames;
  Set<UUID> categoryIds;
}
