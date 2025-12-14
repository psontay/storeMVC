package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.user.UserCreationRequest;
import com.sontaypham.storemvc.dto.request.user.UserRegisterRequest;
import com.sontaypham.storemvc.dto.request.user.UserUpdateProfileRequest;
import com.sontaypham.storemvc.dto.request.user.UserUpdateRequest;
import com.sontaypham.storemvc.dto.response.user.UserCreationResponse;
import com.sontaypham.storemvc.dto.response.user.UserRegisterResponse;
import com.sontaypham.storemvc.dto.response.user.UserResponse;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  UserRegisterResponse registerUser(UserRegisterRequest request);

  UserCreationResponse createUser(UserCreationRequest request);

  UserResponse findByEmail(String email);

  Optional<UserResponse> findById(UUID id);
  UserResponse findByUsername(String username);

  void deleteByUsername(String username);

  void updateUserProfile(String username, UserUpdateProfileRequest request);

  UserResponse updateUser(UserUpdateRequest request);

  void changePassword(UUID id, String oldPassword, String newPassword);

  Page<UserResponse> findAll(Pageable pageable);
    Page<UserResponse> findByUsernameOrEmailContainingIgnoreCase( String keyword , Pageable pageable);
}
