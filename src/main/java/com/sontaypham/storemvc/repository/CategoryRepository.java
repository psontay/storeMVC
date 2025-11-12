package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.model.Category;

import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Nonnull
    Optional<Category> findById(@Nonnull UUID id);
    @Nonnull
    Optional<Category> findByName(@Nonnull String name);
    @Nonnull
    Optional<Category> findByNameIgnoreCase(String name);
    boolean existsByName(String name);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
