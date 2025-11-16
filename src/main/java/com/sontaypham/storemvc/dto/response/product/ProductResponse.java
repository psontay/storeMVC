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

  LocalDateTime createdAt;
  LocalDateTime updatedAt;

  String status;
  String supplier;
  Set<String> categories;
}
