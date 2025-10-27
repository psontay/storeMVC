package com.sontaypham.storemvc.helper;

import com.sontaypham.storemvc.dto.request.role.RoleCreationRequest;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapperHelper {
    private final RoleRepository roleRepository;
    @Named("toRoleObject")
    public Set<Role> toRoleObject(Set<String> roleString){
        if(roleString==null || roleString.isEmpty()) return Set.of();
        return roleString.stream().map(String::toUpperCase).map(roleRepository::findRoleByName).filter(
                Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
    }
}
