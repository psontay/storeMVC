package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.dto.response.order.OrderStats;
import com.sontaypham.storemvc.enums.OrderStatus;
import com.sontaypham.storemvc.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
  @Query(
      value =
          "SELECT COALESCE(SUM(total_price),0) FROM orders WHERE DATE(order_date) = CURRENT_DATE AND order_status = 'SHIPPED'",
      nativeQuery = true)
  BigDecimal getTodayRevenue();

  @Query(
      """
    select o from Order o
     join fetch o.user u
     join fetch o.orderItems oi
     where o.id =: orderId
     and o.user.id = :uuserId
""")
  Optional<Order> getOrderByIdAndUserId(
      @Param("orderId") UUID orderId, @Param("userId") UUID userId);

  Page<Order> findByOrderStatus(OrderStatus status, Pageable pageable);
  
  @Query(
      """
        SELECT o FROM Order o
        JOIN FETCH o.user u
        JOIN FETCH o.orderItems oi
        JOIN FETCH oi.product p
        WHERE o.id = :orderId AND o.user.id = :userId
        """)
  Optional<Order> findOrderDetailByIdAndUserId(
      @Param("orderId") UUID orderId, @Param("userId") UUID userId);

  @Query(
      """
        select new com.sontaypham.storemvc.dto.response.order.OrderStats( count(o) , sum(case when o.orderStatus = 'PENDING' then 1 else 0 end), sum(case when o.orderStatus = 'DELIVERED' then 1 else 0 end)
        )
        from Order o
        where o.user.id = :userId
        group by o.user.id
        """)
  OrderStats getOrderStatsByUserId(UUID userId);

    Optional<Order> findById(UUID id);
    // dashboard
    @Query("select count (o.id) from Order o where o.orderStatus = :status")
    long countByOrderStatus(OrderStatus status);
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderStatus = 'SHIPPED'")
    BigDecimal sumTotalRevenue();

    List<Order> findTop5ByOrderByOrderDateDesc();

    @Query("SELECT cast(o.orderDate as date), SUM(o.totalPrice) " +
           "FROM Order o " +
           "WHERE o.orderStatus = 'SHIPPED' AND o.orderDate >= :startDate " +
           "GROUP BY cast(o.orderDate as date) " +
           "ORDER BY cast(o.orderDate as date) ASC")
    List<Object[]> getRevenueByDate(@Param("startDate") LocalDateTime startDate);
}
