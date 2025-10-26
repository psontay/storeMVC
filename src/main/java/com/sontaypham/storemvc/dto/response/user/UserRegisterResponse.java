package com.sontaypham.storemvc.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {
    String username;
    String email;
    String telPhone;
    String address;
}
