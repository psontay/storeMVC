package com.sontaypham.storemvc.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {
    String to;
    String subject;
    String messageBody;
    String templateName;
    Map<String, Object> variables;
}
