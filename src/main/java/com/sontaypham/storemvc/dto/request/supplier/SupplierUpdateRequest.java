package com.sontaypham.storemvc.dto.request.supplier;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierUpdateRequest {
  String name;
  String fullName;
  String email;
  String telPhone;
}
