package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.enums.ProductStatus;
import com.sontaypham.storemvc.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.Nonnull;
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
    List<Product> findByOrigin( String origin );
    @Query("select p from Product p where p.price <= :price ")
    List<Product> findByPrice( BigDecimal price);
    @Query("select p from Product p where p.status = :status")
    List<Product> findByStatus( ProductStatus status);
    @Query("select p from Product p where p.supplier.name = :supplierName")
    List<Product> findBySupplierName( @Nonnull String supplierName );
    @Query("select p from Product p where p.supplier.id = :supplierId ")
    List<Product> findBySupplierId( @Nonnull UUID supplierId);
    @Query("select p from Product p where p.category.name = :categoryName")
    List<Product> findByCategoryName( String categoryName );
}
