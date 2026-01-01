package com.sontaypham.storemvc.dto.response.dashboard;

import com.sontaypham.storemvc.model.Order;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    long totalUsers;
    long newOrders;
    long lowStockProducts;
    BigDecimal totalRevenue;

    List<String> chartLabels;
    List<BigDecimal> chartData;

    List<Order> recentOrders;
}
