package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct( ProductCreationRequest request);
    ProductResponse updateProduct(UUID id ,  ProductUpdateRequest request);
    ProductResponse findById(UUID id);
    ProductResponse findByName(String name);
    Page<ProductResponse> findByOrigin(String origin , Pageable pageable);
    Page<ProductResponse> findByPrice( BigDecimal price , Pageable pageable);
    Page<ProductResponse> findByStatus( String status , Pageable pageable);
    Page<ProductResponse> findBySupplierId ( UUID supplierId , Pageable pageable);
    Page<ProductResponse> findBySupplierName ( String supplierName , Pageable pageable);
    Page<ProductResponse> findByCategoryName ( String categoryName , Pageable pageable);
    Page<ProductResponse> findAll( Pageable pageable);
}
