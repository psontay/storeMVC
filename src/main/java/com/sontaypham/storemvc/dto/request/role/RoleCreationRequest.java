package com.sontaypham.storemvc.dto.request.role;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreationRequest {
  String name;
  String description;
  Set<String> permissions;
}
