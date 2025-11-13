package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.category.CategoryCreationRequest;
import com.sontaypham.storemvc.dto.request.category.CategoryUpdateRequest;
import com.sontaypham.storemvc.dto.response.category.CategoryResponse;
import com.sontaypham.storemvc.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreationRequest request);
    CategoryResponse updateCategory(CategoryUpdateRequest request);
    CategoryResponse findById(UUID id);
    CategoryResponse findByName(String name);
    CategoryResponse findByNameIgnoreCase(String name, UUID id);
    Page<CategoryResponse> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
