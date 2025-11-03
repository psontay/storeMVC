package com.sontaypham.storemvc.mapper;

import com.sontaypham.storemvc.dto.request.permission.PermissionCreationRequest;
import com.sontaypham.storemvc.dto.response.permission.PermissionResponse;
import com.sontaypham.storemvc.model.Permission;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
