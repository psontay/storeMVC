package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.user.UserCreationRequest;
import com.sontaypham.storemvc.dto.response.user.UserCreationResponse;
import com.sontaypham.storemvc.helper.PermissionMapperHelper;
import com.sontaypham.storemvc.helper.RoleMapperHelper;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper( componentModel = "spring" , uses = {RoleMapperHelper.class , PermissionMapperHelper.class} )
public interface UserCreationMapper {
    @Mapping( target = "roles" , source = "roles" , qualifiedByName = "toRoleObject")
    @Mapping( target = "permissions" , source = "permissions" , qualifiedByName = "toPermissionObject")
    User toUser (UserCreationRequest request);

    @Mapping( target = "roles" , source = "roles" , qualifiedByName = "toRoleString")
    @Mapping( target = "permissions" , source = "permissions" , qualifiedByName = "toPermissionString")
    UserCreationResponse toResponse ( User user);

    // role mapper helper
    @Named("toRoleObject")
    static Set<Role> toRoleObject(Set<String> roleString , @Context RoleMapperHelper roleMapperHelper) {
        return roleMapperHelper.toRoleObject(roleString);
    }
    @Named("toRoleString")
    static Set<String> toRoleString ( Set<Role> roleObject , @Context RoleMapperHelper roleMapperHelper ) {
        return roleMapperHelper.toRoleString(roleObject);
    }

    // permission mapper helper
    @Named("toPermissionObject")
    static Set<Permission> toPermissionObject ( Set<String> permissionString , @Context PermissionMapperHelper permissionMapperHelper ) {
        return permissionMapperHelper.toPermissionObject(permissionString);
    }
    @Named("toPermissionString")
    static Set<String> toPermissionString ( Set<Permission> permissionObject , @Context PermissionMapperHelper permissionMapperHelper ) {
        return permissionMapperHelper.toPermissionString(permissionObject);
    }
}
