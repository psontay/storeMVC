package com.sontaypham.storemvc.dto.request.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

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
    String address;
    Set<String> roles;
    Set<String> permissions;
}
