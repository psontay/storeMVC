package com.sontaypham.storemvc.dto.response.user;

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
public class UserUpdateResponse {
  UUID id;
  String username;
  String fullName;
  String email;
  String telPhone;
  String address;
  Set<String> roles;
  Set<String> permissions;
}
