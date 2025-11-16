package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.response.cart.CartItemResponse;
import com.sontaypham.storemvc.dto.response.cart.CartResponse;
import com.sontaypham.storemvc.model.Cart;
import com.sontaypham.storemvc.model.CartItem;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring")
public interface CartMapper {
    @Mapping( source = "user.id" , target = "userId")
    @Mapping( source = "user.username" , target = "username")
    CartResponse fromEntityToResponse(Cart cart);



    @Mapping( source = "product.id" , target = "productId")
    @Mapping( source = "product.name" , target = "productName")
    @Mapping( source = "product.imageUrl" , target = "productImageUrl")
    @Mapping(
            target = "subtotal",
            expression = "java(cartItem.getUnitPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())))"
    )
    CartItemResponse fromEntityToResponse(CartItem cartItem);
}
