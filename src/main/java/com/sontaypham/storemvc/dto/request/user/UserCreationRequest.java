package com.sontaypham.storemvc.dto.request.user;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// do admin tao nen khong su dung valid
public class UserCreationRequest {
  String username;
  String password;
  String fullName;
  String email;
  String telPhone;
  String address;
  Set<String> roles;
  Set<String> permissions;
}
