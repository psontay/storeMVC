package com.sontaypham.storemvc.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

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
