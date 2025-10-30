package com.sontaypham.storemvc.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateProfileResponse {
    String fullName;
    String email;
    String telPhone;
    String address;
}
