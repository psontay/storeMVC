package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.supplier.SupplierCreationRequest;
import com.sontaypham.storemvc.dto.request.supplier.SupplierUpdateRequest;
import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.dto.response.supplier.SupplierResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.mapper.ProductMapper;
import com.sontaypham.storemvc.mapper.SupplierMapper;
import com.sontaypham.storemvc.model.Product;
import com.sontaypham.storemvc.model.Supplier;
import com.sontaypham.storemvc.repository.SupplierRepository;
import com.sontaypham.storemvc.service.SupplierService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE , makeFinal = true )
public class SupplierServiceImpl implements SupplierService {
    SupplierRepository supplierRepository;
    SupplierMapper supplierMapper;
    ProductMapper  productMapper;
    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierCreationRequest supplierCreationRequest) {
        if (supplierRepository.findByName(supplierCreationRequest.getName()).isPresent()) {
            throw new ApiException(ErrorCode.SUPPLIER_ALREADY_EXISTS);
        }
        Supplier supplier = Supplier.builder().name(supplierCreationRequest.getName()).fullName(supplierCreationRequest.getFullName()).email(supplierCreationRequest.getEmail()).telPhone(supplierCreationRequest.getTelPhone()).build();
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.fromEntityToResponse(saved);
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(UUID id , SupplierUpdateRequest supplierUpdateRequest) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        supplier.setName(supplierUpdateRequest.getName());
        supplier.setFullName(supplierUpdateRequest.getFullName());
        supplier.setEmail(supplierUpdateRequest.getEmail());
        supplier.setTelPhone(supplierUpdateRequest.getTelPhone());
        Supplier updated = supplierRepository.save(supplier);
        return supplierMapper.fromEntityToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSupplierById(UUID id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        supplierRepository.delete(supplier);
    }

    @Override
    @Transactional
    public void deleteSupplierByName(String name) {
        Supplier supplier =  supplierRepository.findByName(name).orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        supplierRepository.delete(supplier);
    }

    @Override
    public SupplierResponse getSupplierById(UUID id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        return supplierMapper.fromEntityToResponse(supplier);
    }

    @Override
    public SupplierResponse getSupplierByName(String name) {
        Supplier supplier = supplierRepository.findByName(name).orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        return supplierMapper.fromEntityToResponse(supplier);
    }

    @Override
    public Set<ProductResponse> getProductBySupplierId(UUID id) {
        Supplier supplier =  supplierRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        Set<Product> products =  supplier.getProducts();
        return products.stream().map(productMapper::fromEntityToResponse).collect(Collectors.toSet());
    }

    @Override
    public Set<ProductResponse> getProductBySupplierName(String name) {
        Supplier supplier = supplierRepository.findByName(name).orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        Set<Product> products =  supplier.getProducts();
        return products.stream().map(productMapper::fromEntityToResponse).collect(Collectors.toSet());
    }
}
