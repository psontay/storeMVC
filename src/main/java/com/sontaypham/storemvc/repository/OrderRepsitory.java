package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepsitory extends JpaRepository<Order, UUID> {
}
