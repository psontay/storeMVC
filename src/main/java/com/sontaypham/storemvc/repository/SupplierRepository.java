package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Supplier;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {}
