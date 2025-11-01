package com.sontaypham.storemvc.helper;

import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionMapperHelper {
    private final PermissionRepository permissionRepository;
    @Named("toPermissionObject")
    public Set<Permission> toPermissionObject ( Set<String> permissionString) {
        if ( permissionString == null || permissionString.isEmpty()) return Set.of();
        return permissionString.stream().map(String::toUpperCase).map(permissionRepository::findByName).filter(Optional::isPresent).map(Optional::get).collect(
                Collectors.toSet());
    }
    @Named("toPermissionString")
    public Set<String> toPermissionString (Set<Permission> permissionObject) {
        return permissionObject.stream().map(Permission::getName).collect(Collectors.toSet());
    }
}
