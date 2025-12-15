package com.sontaypham.storemvc.helper;

import com.sontaypham.storemvc.dto.response.product.ProductResponse;
import com.sontaypham.storemvc.mapper.ProductMapper;
import com.sontaypham.storemvc.model.Product;
import com.sontaypham.storemvc.repository.ProductRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapperHelper {
  ProductMapper productMapper;

  @Named("toResponseObject")
  public Set<ProductResponse> toResponseObject(Set<Product> products) {
    if (products == null || products.isEmpty()) return Set.of();
    return products.stream().map(productMapper::fromEntityToResponse).collect(Collectors.toSet());
  }
}
