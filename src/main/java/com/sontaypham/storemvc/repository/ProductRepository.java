package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.enums.ProductStatus;
import com.sontaypham.storemvc.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Nonnull
    Optional<Product> findById( @Nonnull UUID id);
    @Nonnull
    Optional<Product> findByName(@Nonnull String name);
    Page<Product> findByOrigin( String origin , Pageable pageable);
    @Query("select p from Product p where p.price <= :price ")
    Page<Product> findByPrice( BigDecimal price , Pageable pageable);
    @Query("select p from Product p where p.status = :status")
    Page<Product> findByStatus( ProductStatus status , Pageable pageable);
    @Query("select p from Product p where p.supplier.name = :supplierName")
    Page<Product> findBySupplierName( @Nonnull String supplierName , Pageable pageable);
    @Query("select p from Product p where p.supplier.id = :supplierId ")
    Page<Product> findBySupplierId( @Nonnull UUID supplierId , Pageable pageable);
    @Query("select p from Product p where p.category.name = :categoryName")
    Page<Product> findByCategoryName(String categoryName , Pageable pageable);
    Page<Product> findAll( Pageable pageable);
    boolean existsByName( String name);
}
