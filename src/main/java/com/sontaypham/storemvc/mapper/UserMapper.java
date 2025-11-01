package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.response.user.UserResponse;
import com.sontaypham.storemvc.helper.PermissionMapperHelper;
import com.sontaypham.storemvc.helper.RoleMapperHelper;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import org.mapstruct.*;

import java.util.Set;

@Mapper( componentModel = "spring" , uses = {RoleMapperHelper.class , PermissionMapperHelper.class} )
public interface UserMapper {
    @Mapping( target = "roles" , source = "roles" , qualifiedByName = "toRoleString")
    @Mapping( target = "permissions" , source = "permissions" , qualifiedByName = "toPermissionString")
    UserResponse toUserResponse(User user);
    @Named("toRoleString")
    static Set<String> toRoleString(Set<Role> roleObject , @Context RoleMapperHelper roleMapperHelper) {
        return roleMapperHelper.toRoleString(roleObject);
    }
    @Named("toPermissionString")
    static Set<String> toPermissionString(Set<Permission> permissionObject , @Context PermissionMapperHelper permissionMapperHelper) {
        return  permissionMapperHelper.toPermissionString(permissionObject);
    }

}
