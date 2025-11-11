package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.helper.CategoryMapperHelper;
import com.sontaypham.storemvc.helper.SupplierMapperHelper;
import com.sontaypham.storemvc.model.Category;
import com.sontaypham.storemvc.model.Product;
import com.sontaypham.storemvc.model.Supplier;
import org.mapstruct.*;

import java.util.UUID;

@Mapper( componentModel = "spring" , uses = { SupplierMapperHelper.class , CategoryMapperHelper.class})
public interface ProductMapper {
    @Mapping( target = "supplier" , source = "supplierId" , qualifiedByName = "fromIdentifyToSupplierEntity")
    @Mapping( target = "category" , source = "categoryId" , qualifiedByName = "fromIdentifyToCategoryEntity")
    public Product fromCreationToEntity (ProductCreationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product product);


    @Mapping(target = "supplier" , source = "supplier" , qualifiedByName = "fromSupplierEntityToString")
    @Mapping(target = "category" , source = "category" , qualifiedByName = "fromCategoryEntityToString")
    public ProductResponse fromEntityToResponse (Product entity);



    // helper
    @Named("fromIdentifyToSupplierEntity")
    static Supplier fromIdentifyToSupplierEntity(UUID id , SupplierMapperHelper supplierMapperHelper) {
        return supplierMapperHelper.fromIdentifyToEntity(id);
    }
    @Named("fromIdentifyToCategoryEntity")
    static Category fromIdentityToCategoryEntity(UUID id ,  CategoryMapperHelper categoryMapperHelper) {
        return categoryMapperHelper.fromIdentifyToCategoryEntity(id);
    }
    @Named("fromSupplierEntityToString")
    static String fromSupplierEntityToString (Supplier supplier ,  SupplierMapperHelper supplierMapperHelper) {
        return supplierMapperHelper.fromEntityToString(supplier);
    }
    @Named("fromCategoryEntityToString")
    static String fromCategoryEntityToString (Category category,  CategoryMapperHelper categoryMapperHelper) {
        return categoryMapperHelper.fromEntityToString(category);
    }
}
