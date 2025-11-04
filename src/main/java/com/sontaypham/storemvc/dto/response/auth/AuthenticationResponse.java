package com.sontaypham.storemvc.dto.response.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    UUID id;
    String username;
    String password;
    String email;
    String telPhone;
    String fullName;
    String address;
    List<String> roles;
    List<String> permissions;
}
