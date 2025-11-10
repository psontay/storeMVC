package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.repository.ProductRepository;
import com.sontaypham.storemvc.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductCreationRequest request) {
        return null;
    }

    @Override
    public ProductResponse updateProduct(ProductUpdateRequest request) {
        return null;
    }

    @Override
    public Optional<ProductResponse> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<ProductResponse> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<ProductResponse> findByOrigin(String origin) {
        return List.of();
    }

    @Override
    public List<ProductResponse> findByPrice(BigDecimal price) {
        return List.of();
    }

    @Override
    public List<ProductResponse> findByStatus(String status) {
        return List.of();
    }

    @Override
    public List<ProductResponse> findBySupplierId(UUID supplierId) {
        return List.of();
    }

    @Override
    public List<ProductResponse> findByCategoryName(String categoryName) {
        return List.of();
    }
}
