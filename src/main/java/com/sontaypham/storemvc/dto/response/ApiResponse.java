package com.sontaypham.storemvc.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    String message;
    int status;
    T data;
}
