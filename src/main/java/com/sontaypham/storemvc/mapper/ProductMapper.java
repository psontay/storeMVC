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

    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "supplierId", source = "supplier.id")  // ← THÊM DÒNG NÀY
    @Mapping(target = "categoryNames", expression = "java(product.getCategories().stream().map(cat -> cat.getName()).collect(java.util.stream.Collectors.toSet()))")
    @Mapping(target = "categoryIds", expression = "java(product.getCategories().stream().map(cat -> cat.getId()).collect(java.util.stream.Collectors.toSet()))") // ← THÊM DÒNG NÀY
    ProductResponse fromEntityToResponse(Product product);
}
