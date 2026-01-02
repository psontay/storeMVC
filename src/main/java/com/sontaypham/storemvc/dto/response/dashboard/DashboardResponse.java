package com.sontaypham.storemvc.dto.response.dashboard;

import com.sontaypham.storemvc.model.Order;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
  long totalUsers;
  long newOrders;
  long lowStockProducts;
  BigDecimal totalRevenue;

  List<String> chartLabels;
  List<BigDecimal> chartData;

  List<Order> recentOrders;
}
