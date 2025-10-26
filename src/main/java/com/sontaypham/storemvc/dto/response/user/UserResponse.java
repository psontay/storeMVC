package com.sontaypham.storemvc.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

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
    Set<String> roles;
    Set<String> permissions;
}
