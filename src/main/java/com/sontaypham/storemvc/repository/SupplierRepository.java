package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Supplier;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    Page<Supplier> findByNameContainingIgnoreCase(@Nonnull String name , Pageable pageable);
    Page<Supplier> findAll(Pageable pageable);
  @Nonnull
  Optional<Supplier> findById(@Nonnull UUID id);

  @Nonnull
  Optional<Supplier> findByName(@Nonnull String name);

  Optional<Supplier> findByEmail(@Nonnull String email);

  Optional<Supplier> findByTelPhone(@Nonnull String telPhone);
}
