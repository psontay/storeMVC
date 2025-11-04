package com.sontaypham.storemvc.dto.response.permission;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponse {
  private String name;
  private String description;
}
