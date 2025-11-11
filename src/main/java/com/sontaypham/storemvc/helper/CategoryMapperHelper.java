package com.sontaypham.storemvc.helper;

import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.Category;
import com.sontaypham.storemvc.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryMapperHelper {
    private final CategoryRepository categoryRepository;
    @Named("fromIdentifyToCategoryEntity")
    public Category fromIdentifyToCategoryEntity(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    }
    @Named("fromCategoryEntityToString")
    public String fromEntityToString (Category category) {
        return category.getName();
    }
}
