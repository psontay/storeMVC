package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.category.CategoryCreationRequest;
import com.sontaypham.storemvc.dto.request.category.CategoryUpdateRequest;
import com.sontaypham.storemvc.dto.response.category.CategoryResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.mapper.CategoryMapper;
import com.sontaypham.storemvc.model.Category;
import com.sontaypham.storemvc.repository.CategoryRepository;
import com.sontaypham.storemvc.service.CategoryService;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
  CategoryRepository categoryRepository;
  CategoryMapper categoryMapper;

  @Override
  public CategoryResponse createCategory(CategoryCreationRequest request) {
    if (categoryRepository.existsByName(request.getName()))
      throw new ApiException(ErrorCode.CATEGORY_ALREADY_EXISTS);
    Category category =
        Category.builder().name(request.getName()).description(request.getDescription()).build();
    Category saved = categoryRepository.save(category);
    return categoryMapper.fromEntityToResponse(saved);
  }

  @Override
  public CategoryResponse updateCategory(CategoryUpdateRequest request) {
    Category category =
        categoryRepository
            .findById(request.getId())
            .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    category.setName(request.getName());
    category.setDescription(request.getDescription());
    categoryMapper.updateEntityFromRequest(request, category);
    Category saved = categoryRepository.save(category);
    return categoryMapper.fromEntityToResponse(saved);
  }

  @Override
  public CategoryResponse findById(UUID id) {
    return categoryMapper.fromEntityToResponse(
        categoryRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND)));
  }

  @Override
  public CategoryResponse findByName(String name) {
    return categoryMapper.fromEntityToResponse(
        categoryRepository
            .findByName(name)
            .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND)));
  }

  @Override
  public CategoryResponse findByNameIgnoreCase(String name, UUID id) {
    return categoryMapper.fromEntityToResponse(
        categoryRepository
            .findByNameIgnoreCase(name)
            .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND)));
  }

  @Override
  public Page<CategoryResponse> findByNameContainingIgnoreCase(String name, Pageable pageable) {
    return categoryRepository
        .findByNameContainingIgnoreCase(name, pageable)
        .map(categoryMapper::fromEntityToResponse);
  }
}
