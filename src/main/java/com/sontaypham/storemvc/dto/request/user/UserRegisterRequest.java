package com.sontaypham.storemvc.dto.request.user;

import com.sontaypham.storemvc.validator.EmailConstraint;
import com.sontaypham.storemvc.validator.PasswordConstraint;
import com.sontaypham.storemvc.validator.TelPhoneConstraint;
import com.sontaypham.storemvc.validator.UsernameConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterRequest {
  @UsernameConstraint String username;
  @PasswordConstraint String password;
  String confirmPassword;
  @EmailConstraint String email;
  String fullName;
  @TelPhoneConstraint String telPhone;
  String address;
}
