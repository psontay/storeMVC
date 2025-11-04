package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.auth.AuthenticationRequest;
import com.sontaypham.storemvc.dto.response.auth.AuthenticationResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.UserRepository;
import com.sontaypham.storemvc.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        if ( !(passwordEncoder.matches(request.getPassword(), user.getPassword())) ) throw new ApiException(ErrorCode.PASSWORD_NOT_MATCHES);
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        List<String> permissions = user.getRoles().stream().flatMap( role -> role.getPermissions().stream()).map(Permission::getName).distinct()
                                       .toList();
        // get all roles of this user and transform to SImpleGrantedAuthority then add prefix
        List<SimpleGrantedAuthority> roleAuthorities = user.getRoles().stream().map( role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).toList();
        // get all permissions of this user and transform to SimpleGrantedAuthority
        List<SimpleGrantedAuthority> permissionAuthorities = user.getRoles().stream().flatMap( role -> role.getPermissions().stream())
                .map( permission -> new SimpleGrantedAuthority(permission.getName())).toList();

        List<SimpleGrantedAuthority> allAuthorities = Stream.concat(roleAuthorities.stream(), permissionAuthorities.stream()).toList();
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), null, allAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return AuthenticationResponse.builder().id(user.getId()).username(user.getUsername()).password(user.getPassword()).fullName(user.getFullName()).email(user.getEmail()).telPhone(user.getTelPhone()).address(user.getTelPhone()).roles(roles).permissions(permissions).build();
    }
}
