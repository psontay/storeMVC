package com.sontaypham.storemvc.dto.request.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
  String username;
  String password;
}
