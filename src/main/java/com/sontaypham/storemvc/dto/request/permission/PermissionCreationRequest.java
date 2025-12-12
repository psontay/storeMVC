    package com.sontaypham.storemvc.dto.request.permission;

    import lombok.*;
    import lombok.experimental.FieldDefaults;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class PermissionCreationRequest {
      String name;
      String description;
    }
