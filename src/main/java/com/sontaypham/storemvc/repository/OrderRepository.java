package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Order;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {


    @Query("select coalesce(sum(o.totalPrice),0) from Order o where date(o.orderDate) = current_date and o.orderStatus = 'SHIPPED' ")
    BigDecimal getTodayRevenue();
}
