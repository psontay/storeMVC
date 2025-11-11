package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct( ProductCreationRequest request);
    ProductResponse updateProduct(UUID id ,  ProductUpdateRequest request);
    ProductResponse findById(UUID id);
    ProductResponse findByName(String name);
    List<ProductResponse> findByOrigin( String origin);
    List<ProductResponse> findByPrice( BigDecimal price);
    List<ProductResponse> findByStatus( String status);
    List<ProductResponse> findBySupplierId ( UUID supplierId);
    List<ProductResponse> findBySupplierName ( String supplierName);
    List<ProductResponse> findByCategoryName ( String categoryName);
}
