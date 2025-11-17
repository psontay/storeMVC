package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Order;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query(value = "SELECT COALESCE(SUM(total_price),0) FROM orders WHERE DATE(order_date) = CURRENT_DATE AND order_status = 'SHIPPED'", nativeQuery = true)
    BigDecimal getTodayRevenue();


}
