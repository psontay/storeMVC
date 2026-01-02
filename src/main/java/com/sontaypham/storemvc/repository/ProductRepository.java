package com.sontaypham.storemvc.repository;

import com.sontaypham.storemvc.enums.ProductStatus;
import com.sontaypham.storemvc.model.Product;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
  @Nonnull
  Optional<Product> findById(@Nonnull UUID id);

  @Nonnull
  Optional<Product> findByName(@Nonnull String name);

  @Query(
      """
    select p from Product p where (:keyword is null or :keyword = '' or lower(p.name) like lower(concat('%' , p.name , '%') ) ) and (:minPrice is null or p.price >= :minPrice) and (:maxPrice is null or p.price <= :maxPrice)
""")
  Page<Product> searchProducts(
      @Param("keyword") String keyword,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      Pageable pageable);

  Page<Product> findByOrigin(String origin, Pageable pageable);

  @Query("select p from Product p where p.price <= :price ")
  Page<Product> findByPrice(BigDecimal price, Pageable pageable);

  @Query("select p from Product p where p.status = :status")
  Page<Product> findByStatus(ProductStatus status, Pageable pageable);

  @Query("select p from Product p where p.supplier.name = :supplierName")
  Page<Product> findBySupplierName(@Nonnull String supplierName, Pageable pageable);

  @Query("select p from Product p where p.supplier.id = :supplierId ")
  Page<Product> findBySupplierId(@Nonnull UUID supplierId, Pageable pageable);

  @Query("select p from Product p join Category c where c.name = :categoryName")
  Page<Product> findByCategoryName(String categoryName, Pageable pageable);

  @Query("select p from Product p join p.categories c where c.id = :categoryId")
  Page<Product> findByCategoryId(@Nonnull UUID categoryId, Pageable pageable);

  Page<Product> findAll(Pageable pageable);

  boolean existsByName(String name);

  Page<Product> findByNameContaining(@Nonnull String name, Pageable pageable);

  Page<Product> findByNameContainingIgnoreCase(@Nonnull String name, Pageable pageable);

  @Query(
      """
    select p from Product p where lower(p.name) like lower(concat('%', :productName , '%') ) or  lower(p.supplier.name) like lower(concat('%', :supplierName , '%'))
""")
  Page<Product> findByProductNameOrSupplierNameContainingIgnoreCase(
      @Nonnull String productName, @Nonnull String supplierName, Pageable pageable);

  // delete feat
  @Query(
      value = "select * from products where deleted_at is not  null  ",
      countQuery = "select count(*) from products where deleted_at is not null",
      nativeQuery = true)
  Page<Product> findAllDeleted(Pageable pageable);

  @Modifying
  @Transactional
  @Query(value = "UPDATE products SET deleted_at = NULL WHERE id = :id", nativeQuery = true)
  void restoreProduct(@Param("id") UUID id);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM products WHERE id = :id", nativeQuery = true)
  void hardDeleteProduct(@Param("id") UUID id);

  // dashboard
  long countByStockQuantityLessThan(int stockQuantity);
}
