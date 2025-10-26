package com.sontaypham.storemvc.dto.request.user;

import com.sontaypham.storemvc.validator.EmailConstraint;
import com.sontaypham.storemvc.validator.TelPhoneConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateProfileRequest {
    String fullName;
    @EmailConstraint
    String email;
    @TelPhoneConstraint
    String telPhone;
    String address;
}
