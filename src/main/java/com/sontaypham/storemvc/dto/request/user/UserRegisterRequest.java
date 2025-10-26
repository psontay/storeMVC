package com.sontaypham.storemvc.dto.request.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class UserRegisterRequest {
    String username;
    String password;
    String confirmPassword;
    String email;
    String fullName;
    String telPhone;
}
