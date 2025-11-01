package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.role.RoleCreationRequest;
import com.sontaypham.storemvc.dto.response.role.RoleResponse;
import com.sontaypham.storemvc.helper.PermissionMapperHelper;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.Role;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper( componentModel = "spring" , uses = {PermissionMapperHelper.class})
public interface RoleMapper {
    @Mapping( target = "permissions" , source = "permissions" , qualifiedByName = "toPermissionObject")
    Role toRole(RoleCreationRequest request);

    @Mapping( target = "permissions" , source = "permissions" , qualifiedByName = "toPermissionString")
    RoleResponse toRoleResponse( Role role);

    @Named("toPermissionObject")
    static Set<Permission> toPermissionObject(Set<String> permissionString , @Context PermissionMapperHelper permissionMapperHelper) {
        return permissionMapperHelper.toPermissionObject(permissionString);
    }
    @Named("toPermissionString")
    static Set<String> toPermissionString(Set<Permission> permissionObject , @Context PermissionMapperHelper permissionMapperHelper) {
        return permissionMapperHelper.toPermissionString(permissionObject);
    }
}
