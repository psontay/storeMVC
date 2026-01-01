package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.response.dashboard.DashboardResponse;
import com.sontaypham.storemvc.enums.OrderStatus;
import com.sontaypham.storemvc.model.Order;
import com.sontaypham.storemvc.repository.OrderRepository;
import com.sontaypham.storemvc.repository.ProductRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import com.sontaypham.storemvc.service.DashboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardServiceImpl implements DashboardService {
    UserRepository userRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;

    @Override
    public DashboardResponse getDashboard() {
        long totalUsers = userRepository.count();
        long newOrders = orderRepository.countByOrderStatus(OrderStatus.PENDING);
        long lowStock = productRepository.countByStockQuantityLessThan(5);
        BigDecimal revenue = orderRepository.sumTotalRevenue();
        if (revenue == null) revenue = BigDecimal.ZERO;

        List<Order> recentOrders = orderRepository.findTop5ByOrderByOrderDateDesc();

        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        List<Object[]> rawChartData = orderRepository.getRevenueByDate(startDate.atStartOfDay());

        Map<LocalDate, BigDecimal> revenueMap = rawChartData.stream()
                                                            .collect(Collectors.toMap(
                                                                    obj -> ((Date) obj[0]).toLocalDate(),
                                                                    obj -> (BigDecimal) obj[1]
                                                                                     ));

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate date = startDate.plusDays(i);

            labels.add(date.format(formatter));
            data.add(revenueMap.getOrDefault(date, BigDecimal.ZERO));
        }

        return DashboardResponse.builder()
                                .totalUsers(totalUsers)
                                .newOrders(newOrders)
                                .lowStockProducts(lowStock)
                                .totalRevenue(revenue)
                                .recentOrders(recentOrders)
                                .chartLabels(labels)
                                .chartData(data)
                                .build();
    }
}