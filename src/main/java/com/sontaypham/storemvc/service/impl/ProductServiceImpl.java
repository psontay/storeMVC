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
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
  ProductRepository productRepository;
  CategoryRepository categoryRepository;
  SupplierRepository supplierRepository;
  ProductMapper productMapper;

  @Override
  @Transactional
  public ProductResponse createProduct(ProductCreationRequest request) {
    if (productRepository.existsByName(request.getName()))
      throw new ApiException(ErrorCode.PRODUCT_ALREADY_EXISTS);
    Supplier supplier =
        supplierRepository
            .findById(request.getSupplierId())
            .orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
    Set<UUID> categoryId = request.getCategoryId();
    Set<Category> categories =
        categoryId.stream()
            .map(
                c ->
                    categoryRepository
                        .findById(c)
                        .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND)))
            .collect(Collectors.toSet());
    Product product =
        Product.builder()
            .name(request.getName())
            .description(request.getDescription())
            .age(request.getAge())
            .origin(request.getOrigin())
            .imageUrl(request.getImageUrl())
            .stockQuantity(request.getStockQuantity())
            .price(request.getPrice())
            .discountedPrice(request.getDiscountedPrice())
            .originalPrice(request.getOriginalPrice())
            .discountPercent(request.getDiscountPercent())
            .status(request.getStatus())
            .supplier(supplier)
            .categories(categories)
            .build();
    Product saved = productRepository.save(product);
    return productMapper.fromEntityToResponse(saved);
  }

  @Override
  @Transactional
  public ProductResponse updateProduct(UUID id, ProductUpdateRequest request) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    if (!request.getCategory().isEmpty()) {
      Set<UUID> categoryId = request.getCategory();
      Set<Category> categories =
          categoryId.stream()
              .map(
                  c ->
                      categoryRepository
                          .findById(c)
                          .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND)))
              .collect(Collectors.toSet());
      product.setCategories(categories);
    }
    if (request.getSupplierId() != null) {
      Supplier supplier =
          supplierRepository
              .findById(request.getSupplierId())
              .orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
      product.setSupplier(supplier);
    }
    productMapper.updateEntityFromRequest(request, product);
    Product saved = productRepository.save(product);
    return productMapper.fromEntityToResponse(saved);
  }

  @Override
  public void addCategoryToProduct(UUID id, UUID categoryId) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    Category category =
        categoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    product.getCategories().add(category);
    category.getProducts().add(product);
    productRepository.save(product);
  }

  @Override
  public ProductResponse findById(UUID id) {
    return productRepository
        .findById(id)
        .map(productMapper::fromEntityToResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
  }

  @Override
  public ProductResponse findByName(String name) {
    return productRepository
        .findByName(name)
        .map(productMapper::fromEntityToResponse)
        .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
  }

  @Override
  public Page<ProductResponse> findByOrigin(String origin, Pageable pageable) {
    return productRepository
        .findByOrigin(origin, pageable)
        .map(productMapper::fromEntityToResponse);
  }

  @Override
  public Page<ProductResponse> findByPrice(BigDecimal price, Pageable pageable) {
    return productRepository.findByPrice(price, pageable).map(productMapper::fromEntityToResponse);
  }

  @Override
  public Page<ProductResponse> findByStatus(String status, Pageable pageable) {
    ProductStatus productStatus;
    try {
      productStatus = ProductStatus.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ApiException(ErrorCode.INVALID_PRODUCT_STATUS);
    }
    return productRepository
        .findByStatus(productStatus, pageable)
        .map(productMapper::fromEntityToResponse);
  }

  @Override
  public Page<ProductResponse> findBySupplierId(UUID supplierId, Pageable pageable) {
    return productRepository
        .findBySupplierId(supplierId, pageable)
        .map(productMapper::fromEntityToResponse);
  }

  @Override
  public Page<ProductResponse> findBySupplierName(String supplierName, Pageable pageable) {
    return productRepository
        .findBySupplierName(supplierName, pageable)
        .map(productMapper::fromEntityToResponse);
  }

  @Override
  public Page<ProductResponse> findByCategoryName(String categoryName, Pageable pageable) {
    return productRepository
        .findByCategoryName(categoryName, pageable)
        .map(productMapper::fromEntityToResponse);
  }

  @Override
  public Page<ProductResponse> findByCategoryId(UUID categoryId, Pageable pageable) {
    return productRepository
        .findByCategoryId(categoryId, pageable)
        .map(productMapper::fromEntityToResponse);
  }

  @Override
  public Page<ProductResponse> findAll(Pageable pageable) {
    return productRepository.findAll(pageable).map(productMapper::fromEntityToResponse);
  }
}
