package com.sontaypham.storemvc.dto.request.contact;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactRequest {
  String fullName;
  String email;
  String message;
}
