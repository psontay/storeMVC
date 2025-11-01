package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.user.UserCreationRequest;
import com.sontaypham.storemvc.dto.request.user.UserRegisterRequest;
import com.sontaypham.storemvc.dto.request.user.UserUpdateProfileRequest;
import com.sontaypham.storemvc.dto.request.user.UserUpdateRequest;
import com.sontaypham.storemvc.dto.response.user.UserCreationResponse;
import com.sontaypham.storemvc.dto.response.user.UserRegisterResponse;
import com.sontaypham.storemvc.dto.response.user.UserResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.enums.RoleName;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.helper.PermissionMapperHelper;
import com.sontaypham.storemvc.helper.RoleMapperHelper;
import com.sontaypham.storemvc.mapper.RoleMapper;
import com.sontaypham.storemvc.mapper.UserCreationMapper;
import com.sontaypham.storemvc.mapper.UserMapper;
import com.sontaypham.storemvc.mapper.UserRegisterMapper;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.RoleRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import com.sontaypham.storemvc.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserRegisterMapper userRegisterMapper;
    RoleMapper roleMapper;
    UserCreationMapper userCreationMapper;
    RoleMapperHelper roleMapperHelper;
    PermissionMapperHelper  permissionMapperHelper;
    UserMapper userMapper;

    @Override
    @Transactional
    public UserRegisterResponse registerUser(  @Valid UserRegisterRequest request) {
        if ( userRepository.existsByUsername(request.getUsername()) ) throw new ApiException(ErrorCode.USERNAME_ALREADY_EXISTS);
        if ( !request.getPassword().equals(request.getConfirmPassword()) ) throw new ApiException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCHES);

        Role role = roleRepository.findRoleByName(RoleName.USER.name()).orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                        .username(request.getPassword()).password(passwordEncoder.encode(request.getPassword())).email(request.getEmail()).fullName(request.getFullName()).telPhone(request.getTelPhone()).address(
                        request.getAddress()).roles(roles).build();
        User saved  = userRepository.save(user);
        return userRegisterMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserCreationResponse createUser(UserCreationRequest request) {
        if ( userRepository.existsByUsername(request.getUsername()) ) throw new ApiException(ErrorCode.USERNAME_ALREADY_EXISTS);
        Set<Role> roles = UserCreationMapper.toRoleObject(request.getRoles() , roleMapperHelper);
        Set<Permission>  permissions = UserCreationMapper.toPermissionObject(request.getPermissions() ,
                                                                             permissionMapperHelper);
        User user =
                User.builder().username(request.getUsername()).password(passwordEncoder.encode(request.getPassword())).fullName(request.getFullName()).email(request.getEmail()).telPhone(request.getTelPhone()).address(request.getAddress()).roles(roles).permissions(permissions).build();
        User saved = userRepository.save(user);
        return userCreationMapper.toResponse(saved);
    }

    @Override
    public UserResponse findByEmail(String email) {
        return userMapper.toUserResponse( userRepository.findByEmail(email).orElseThrow( () -> new ApiException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return userMapper.toUserResponse(userRepository.findByUsername(username).orElseThrow( () -> new ApiException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest request) {

        return null;
    }

    @Override
    public void updateUserProfile(UserUpdateProfileRequest request) {

    }

    @Override
    public void changePassword(UUID id, String oldPassword, String newPassword) {

    }
}
