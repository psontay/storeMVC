package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.product.ProductCreationRequest;
import com.sontaypham.storemvc.dto.request.product.ProductUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.helper.CategoryMapperHelper;
import com.sontaypham.storemvc.helper.SupplierMapperHelper;
import com.sontaypham.storemvc.model.Product;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    uses = {SupplierMapperHelper.class, CategoryMapperHelper.class})
public interface ProductMapper {

  @Mapping(
      target = "supplier",
      source = "supplierId",
      qualifiedByName = "fromIdentifyToSupplierEntity")
  @Mapping(
      target = "categories",
      source = "categoryId",
      qualifiedByName = "fromIdentifyToCategoryEntity")
  Product fromCreationToEntity(ProductCreationRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product product);

  @Mapping(target = "supplier", source = "supplier", qualifiedByName = "fromSupplierEntityToString")
  @Mapping(target = "categories", source = "categories", qualifiedByName = "categorySetToStringSet")
  ProductResponse fromEntityToResponse(Product product);
}
