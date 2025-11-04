package com.sontaypham.storemvc.dto.request.user;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
  String username;
  String password;
  String fullName;
  String telPhone;
  String email;
  String address;
  Set<String> roles;
  Set<String> permissions;
}
