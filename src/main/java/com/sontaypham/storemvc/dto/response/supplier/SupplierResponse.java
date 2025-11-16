package com.sontaypham.storemvc.dto.response.supplier;

import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierResponse {
  UUID id;
  String name;
  String fullName;
  String email;
  String telPhone;
  Set<ProductResponse> products;
}
