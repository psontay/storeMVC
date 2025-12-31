package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import jakarta.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
  ProductResponse createProduct(ProductCreationRequest request);

  ProductResponse updateProduct(UUID id, ProductUpdateRequest request);

  void addCategoryToProduct(UUID id, UUID categoryId);

  ProductResponse findById(UUID id);

  ProductResponse findByName(String name);

  Page<ProductResponse> searchProducts(String keyword, BigDecimal minPrice , BigDecimal maxPrice, Pageable pageable );

  Page<ProductResponse> findByOrigin(String origin, Pageable pageable);

  Page<ProductResponse> findByPrice(BigDecimal price, Pageable pageable);

  Page<ProductResponse> findByStatus(String status, Pageable pageable);

  Page<ProductResponse> findBySupplierId(UUID supplierId, Pageable pageable);

  Page<ProductResponse> findBySupplierName(String supplierName, Pageable pageable);

  Page<ProductResponse> findByCategoryName(String categoryName, Pageable pageable);

  Page<ProductResponse> findByCategoryId(UUID categoryId, Pageable pageable);

  Page<ProductResponse> findAll(Pageable pageable);

  Page<ProductResponse> findByNameContaining(@Nonnull String name, Pageable pageable);

  Page<ProductResponse> findByNameContainingIgnoreCase(@Nonnull String name, Pageable pageable);

  Page<ProductResponse> findByProductNameOrSupplierNameContainingIgnoreCase(@Nonnull String name, @Nonnull String supplierName, Pageable pageable);

  void delete(UUID id);
  void hardDelete(UUID id);
  Page<ProductResponse> getTrash(Pageable pageable);
  void restore(UUID id);
}
