package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.auth.UpdatePasswordRequest;
import com.sontaypham.storemvc.dto.request.user.*;
import com.sontaypham.storemvc.dto.response.user.UserCreationResponse;
import com.sontaypham.storemvc.dto.response.user.UserRegisterResponse;
import com.sontaypham.storemvc.dto.response.user.UserResponse;
import com.sontaypham.storemvc.enums.ForgotPasswordStatus;
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

  void changePassword(UUID id, ChangePasswordRequest changePasswordRequest);

  ForgotPasswordStatus forgotPassword(String email);

  void resetPassword(UpdatePasswordRequest updatePasswordRequest);

  boolean isValidatePasswordResetToken(String token);

  Page<UserResponse> findAll(Pageable pageable);

  Page<UserResponse> findByUsernameOrEmailContainingIgnoreCase(String keyword, Pageable pageable);

  Page<UserResponse> findAllDeleted(Pageable pageable);

  void restore(UUID id);

  void hardDelete(UUID id);
}
