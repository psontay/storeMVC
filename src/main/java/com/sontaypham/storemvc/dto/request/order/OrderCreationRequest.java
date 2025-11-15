package com.sontaypham.storemvc.dto.request.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    UUID userId;
    String shippingAddress;
    List<UUID> selectedCartItemIds;
}
