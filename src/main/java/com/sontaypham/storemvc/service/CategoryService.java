package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.category.CategoryCreationRequest;
import com.sontaypham.storemvc.dto.request.category.CategoryUpdateRequest;
import com.sontaypham.storemvc.dto.response.category.CategoryResponse;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
  CategoryResponse createCategory(CategoryCreationRequest request);

  CategoryResponse updateCategory(CategoryUpdateRequest request);

  CategoryResponse findById(UUID id);

  CategoryResponse findByName(String name);

  CategoryResponse findByNameIgnoreCase(String name, UUID id);

  Page<CategoryResponse> findByNameContainingIgnoreCase(String name, Pageable pageable);

  List<CategoryResponse> findAll();
    Page<CategoryResponse> findAll(Pageable pageable);
    void detele(UUID id);
}
