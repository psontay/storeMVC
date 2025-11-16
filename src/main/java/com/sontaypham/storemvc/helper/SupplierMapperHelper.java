package com.sontaypham.storemvc.helper;

import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.Supplier;
import com.sontaypham.storemvc.repository.SupplierRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierMapperHelper {
  private final SupplierRepository supplierRepository;

  @Named("fromIdentifyToSupplierEntity")
  public Supplier fromIdentifyToEntity(UUID id) {
    return supplierRepository
        .findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
  }

  @Named("fromSupplierEntityToString")
  public String fromEntityToString(Supplier supplier) {
    return supplier.getName();
  }
}
