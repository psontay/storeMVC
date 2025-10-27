package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.user.UserRegisterRequest;
import com.sontaypham.storemvc.dto.response.user.UserRegisterResponse;
import com.sontaypham.storemvc.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRegisterMapper {
    @Mapping( target = "id" , ignore = true )
    @Mapping( target = "roles" , ignore = true )
    @Mapping( target = "permissions" , ignore = true )
    @Mapping( target = "username" , source = "username")
    @Mapping( target = "password" , source = "password")
    @Mapping( target = "email" , source = "email")
    @Mapping( target = "fullName" , source = "fullName")
    @Mapping( target = "telPhone" , source = "telPhone")
    @Mapping( target = "address" , source = "address")
    User toUser(UserRegisterRequest request);
    @Mapping( target = "username" , source = "username")
    @Mapping( target = "email" , source = "email")
    @Mapping( target = "telPhone" , source = "telPhone")
    @Mapping( target = "address" , source = "address")
    UserRegisterResponse toResponse(User user);
}
