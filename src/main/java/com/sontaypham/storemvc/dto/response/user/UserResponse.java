package com.sontaypham.storemvc.dto.response.user;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  String username;
  String password;
  String fullName;
  String email;
  String telPhone;
  String address;
  Set<String> roles;
  Set<String> permissions;
}
