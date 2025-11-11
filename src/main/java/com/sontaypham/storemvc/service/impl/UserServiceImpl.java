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
import com.sontaypham.storemvc.mapper.UserCreationMapper;
import com.sontaypham.storemvc.mapper.UserMapper;
import com.sontaypham.storemvc.mapper.UserRegisterMapper;
import com.sontaypham.storemvc.model.Permission;
import com.sontaypham.storemvc.model.Role;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.PermissionRepository;
import com.sontaypham.storemvc.repository.RoleRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import com.sontaypham.storemvc.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  PasswordEncoder passwordEncoder;
  UserRepository userRepository;
  RoleRepository roleRepository;
  PermissionRepository permissionRepository;
  UserRegisterMapper userRegisterMapper;
  UserCreationMapper userCreationMapper;
  RoleMapperHelper roleMapperHelper;
  PermissionMapperHelper permissionMapperHelper;
  UserMapper userMapper;

  @Override
  @Transactional
  public UserRegisterResponse registerUser(@Valid UserRegisterRequest request) {
    if (userRepository.existsByUsername(request.getUsername()))
      throw new ApiException(ErrorCode.USERNAME_ALREADY_EXISTS);
    if (!request.getPassword().equals(request.getConfirmPassword()))
      throw new ApiException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCHES);

    Role role =
        roleRepository
            .findByName(RoleName.USER.name())
            .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
    Set<Role> roles = new HashSet<>();
    roles.add(role);
    User user =
        User.builder()
            .username(request.getPassword())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .fullName(request.getFullName())
            .telPhone(request.getTelPhone())
            .address(request.getAddress())
            .roles(roles)
            .build();
    User saved = userRepository.save(user);
    return userRegisterMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public UserCreationResponse createUser(UserCreationRequest request) {
    if (userRepository.existsByUsername(request.getUsername()))
      throw new ApiException(ErrorCode.USERNAME_ALREADY_EXISTS);
    Set<Role> roles = UserCreationMapper.toRoleObject(request.getRoles(), roleMapperHelper);
    Set<Permission> permissions =
        UserCreationMapper.toPermissionObject(request.getPermissions(), permissionMapperHelper);
    User user =
        User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .email(request.getEmail())
            .telPhone(request.getTelPhone())
            .address(request.getAddress())
            .roles(roles)
            .permissions(permissions)
            .build();
    User saved = userRepository.save(user);
    return userCreationMapper.toResponse(saved);
  }

  @Override
  public UserResponse findByEmail(String email) {
    return userMapper.toUserResponse(
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND)));
  }

  @Override
  public UserResponse findByUsername(String username) {
    return userMapper.toUserResponse(
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND)));
  }

  @Override
  @Transactional
  public void deleteByUsername(String username) {
    userRepository.deleteByUsername(username);
  }

  @Override
  @Transactional
  public UserResponse updateUser(UserUpdateRequest request) {
    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setFullName(request.getFullName());
    user.setTelPhone(request.getTelPhone());
    user.setAddress(request.getAddress());
    user.setEmail(request.getEmail());
    user.setRoles(
        request.getRoles().stream()
            .map(
                name ->
                    roleRepository
                        .findByName(name)
                        .orElseThrow(
                            () -> {
                              log.error("Role Not Found : {}", name);
                              return new ApiException(ErrorCode.ROLE_NOT_FOUND);
                            }))
            .collect(Collectors.toSet()));
    user.setPermissions(
        request.getPermissions().stream()
            .map(
                name ->
                    permissionRepository
                        .findByName(name)
                        .orElseThrow(
                            () -> {
                              log.error("Permission Not Found : {}", name);
                              return new ApiException(ErrorCode.PERMISSION_NOT_FOUND);
                            }))
            .collect(Collectors.toSet()));
    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Override
  @Transactional
  public void updateUserProfile(String username, UserUpdateProfileRequest request) {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    user.setFullName(request.getFullName());
    user.setTelPhone(request.getTelPhone());
    user.setAddress(request.getAddress());
    user.setEmail(request.getEmail());
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void changePassword(UUID id, String oldPassword, String newPassword) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    if (!passwordEncoder.matches(oldPassword, user.getPassword()))
      throw new ApiException(ErrorCode.PASSWORD_NOT_MATCHES);
    user.setPassword(passwordEncoder.encode(newPassword));
  }

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }
}
