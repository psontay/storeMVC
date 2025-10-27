package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.user.UserCreationRequest;
import com.sontaypham.storemvc.dto.response.user.UserCreationResponse;
import com.sontaypham.storemvc.helper.RoleMapperHelper;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper( componentModel = "spring" , uses = {RoleMapperHelper.class})
public interface UserCreationMapper {
    @Mapping( target = "roles" , source = "roles" , qualifiedByName = "toRoleObject")
    @Mapping( target = "permissions" , ignore = true )
    User toUser (UserCreationRequest request);


    UserCreationResponse toResponse ( User user);

    @Named("toRoleObject")
    static Set<Role> toRoleObject(Set<String> roleString , @Context RoleMapperHelper roleMapperHelper) {
        return roleMapperHelper.toRoleObject(roleString);
    }



}
