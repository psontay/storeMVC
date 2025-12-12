package com.sontaypham.storemvc.dto.response.supplier;

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
}
