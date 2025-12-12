package com.sontaypham.storemvc.dto.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sontaypham.storemvc.enums.OrderStatus;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
  UUID orderId;
  String userEmail;
  OrderStatus orderStatus;
  String shippingAddress;
  BigDecimal totalPrice;
  LocalDateTime orderDate;
  Set<OrderItemResponse> orderItems;

  @JsonProperty("formattedTotalPrice")
  public String getFormattedTotalPrice() {
    return "₫" + NumberFormat.getInstance(new Locale("vi", "VN")).format(totalPrice);
  }

  public String getFormattedOrderDate() {
    return orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
  }
}
