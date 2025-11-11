package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.enums.ProductStatus;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.mapper.ProductMapper;
import com.sontaypham.storemvc.model.Category;
import com.sontaypham.storemvc.model.Product;
import com.sontaypham.storemvc.model.Supplier;
import com.sontaypham.storemvc.repository.CategoryRepository;
import com.sontaypham.storemvc.repository.ProductRepository;
import com.sontaypham.storemvc.repository.SupplierRepository;
import com.sontaypham.storemvc.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
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
    CategoryRepository categoryRepository;
    SupplierRepository supplierRepository;
    ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductCreationRequest request) {
        if ( productRepository.existsByName( request.getName() ) )
            throw new ApiException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        Supplier supplier = supplierRepository.findById(request.getSupplierId()).orElseThrow( () -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow( () -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
        Product product =
                Product.builder().name(request.getName()).description(request.getDescription()).age(request.getAge())
                       .origin(request.getOrigin())
                        .imageUrl(request.getImageUrl()).
        stockQuantity(request.getStockQuantity()).price(request.getPrice()).discountedPrice(request.getDiscountedPrice()).originalPrice(request.getOriginalPrice()).discountPercent(request.getDiscountPercent())
                        .status(request.getStatus()).category(category).supplier(supplier).build();
        Product saved = productRepository.save(product);
        return productMapper.fromEntityToResponse(saved);
    }

    @Override
    public ProductResponse updateProduct(UUID id , ProductUpdateRequest request) {
        Product product = productRepository.findById(id).orElseThrow( () -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        if ( request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow( () -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
            product.setCategory(category);
        }
        if ( request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId()).orElseThrow( () -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
            product.setSupplier(supplier);
        }
        productMapper.updateEntityFromRequest(request, product);
        product = productRepository.save(product);
        return productMapper.fromEntityToResponse(product);
    }

    @Override
    public ProductResponse findById(UUID id) {
        return productRepository.findById(id).map(productMapper::fromEntityToResponse).orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public ProductResponse findByName(String name) {
        return productRepository.findByName(name).map(productMapper::fromEntityToResponse).orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public List<ProductResponse> findByOrigin(String origin) {
        return productRepository.findByOrigin(origin).stream().map(productMapper::fromEntityToResponse).toList();
    }

    @Override
    public List<ProductResponse> findByPrice(BigDecimal price) {
        return productRepository.findByPrice(price).stream().map(productMapper::fromEntityToResponse).toList();
    }

    @Override
    public List<ProductResponse> findByStatus(String status) {
        ProductStatus productStatus;
        try {
            productStatus = ProductStatus.valueOf(status.toUpperCase());
        }catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_PRODUCT_STATUS);
        }
        return productRepository.findByStatus(productStatus).stream().map(productMapper::fromEntityToResponse).toList();
    }

    @Override
    public List<ProductResponse> findBySupplierId(UUID supplierId) {
        return productRepository.findBySupplierId(supplierId).stream().map(productMapper::fromEntityToResponse).toList();
    }

    @Override
    public List<ProductResponse> findBySupplierName(String supplierName) {
        return productRepository.findBySupplierName(supplierName).stream().map(productMapper::fromEntityToResponse).toList();
    }

    @Override
    public List<ProductResponse> findByCategoryName(String categoryName) {
        return productRepository.findByCategoryName(categoryName).stream().map(productMapper::fromEntityToResponse).toList();
    }
}
