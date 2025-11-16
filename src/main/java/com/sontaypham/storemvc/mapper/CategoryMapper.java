package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.category.CategoryCreationRequest;
import com.sontaypham.storemvc.dto.request.category.CategoryUpdateRequest;
import com.sontaypham.storemvc.dto.response.category.CategoryResponse;
import com.sontaypham.storemvc.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  public Category fromCreationToEntity(CategoryCreationRequest request);

  void updateEntityFromRequest(CategoryUpdateRequest request, @MappingTarget Category category);

  public CategoryResponse fromEntityToResponse(Category entity);
}
