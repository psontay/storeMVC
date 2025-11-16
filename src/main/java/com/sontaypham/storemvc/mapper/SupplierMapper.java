package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.supplier.SupplierCreationRequest;
import com.sontaypham.storemvc.dto.request.supplier.SupplierUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.dto.response.supplier.SupplierResponse;
import com.sontaypham.storemvc.helper.ProductMapperHelper;
import com.sontaypham.storemvc.model.Product;
import com.sontaypham.storemvc.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;

@Mapper( componentModel = "spring" , uses = {ProductMapperHelper.class})
public interface SupplierMapper {
    Supplier fromCreationToEntity(SupplierCreationRequest supplierCreationRequest);
    void updateFromEntity(SupplierUpdateRequest supplierUpdateRequest, @MappingTarget Supplier supplier);
    @Mapping( target = "products" , source = "products" , qualifiedByName = "toResponseObject")
    SupplierResponse fromEntityToResponse(Supplier supplier);
    @Named("toResponseObject")
    static Set<ProductResponse> toResponseObject(Set<Product> products , ProductMapperHelper productMapperHelper) {
        return productMapperHelper.toResponseObject(products);
    }
}
