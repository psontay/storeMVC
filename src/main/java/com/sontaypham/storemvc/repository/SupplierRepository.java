package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Supplier;

import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    @Nonnull
    Optional<Supplier> findById(@Nonnull UUID id);
    @Nonnull
    Optional<Supplier> findByName(@Nonnull String name);
    Optional<Supplier> findByEmail(@Nonnull String email);
    Optional<Supplier> findByTelPhone(@Nonnull String telPhone);
}
