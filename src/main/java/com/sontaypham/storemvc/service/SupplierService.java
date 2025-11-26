package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.supplier.SupplierCreationRequest;
import com.sontaypham.storemvc.dto.request.supplier.SupplierUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.dto.response.supplier.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SupplierService {
  SupplierResponse createSupplier(SupplierCreationRequest supplierCreationRequest);

  SupplierResponse updateSupplier(UUID id, SupplierUpdateRequest supplierUpdateRequest);

  void deleteSupplierById(UUID id);

  void deleteSupplierByName(String name);

  SupplierResponse getSupplierById(UUID id);

  SupplierResponse getSupplierByName(String name);

  Set<ProductResponse> getProductBySupplierId(UUID id);

  Set<ProductResponse> getProductBySupplierName(String name);
  List<SupplierResponse> findAll();
    Page<SupplierResponse> findAll(Pageable pageable);

    Page<SupplierResponse> findByNameContaining(String name, Pageable pageable);
}
