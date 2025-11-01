package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.request.user.UserCreationRequest;
import com.sontaypham.storemvc.dto.request.user.UserRegisterRequest;
import com.sontaypham.storemvc.dto.request.user.UserUpdateProfileRequest;
import com.sontaypham.storemvc.dto.request.user.UserUpdateRequest;
import com.sontaypham.storemvc.dto.response.user.UserCreationResponse;
import com.sontaypham.storemvc.dto.response.user.UserRegisterResponse;
import com.sontaypham.storemvc.dto.response.user.UserResponse;
import com.sontaypham.storemvc.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserRegisterResponse registerUser(UserRegisterRequest request);
    UserCreationResponse createUser(UserCreationRequest request);
    UserResponse findByEmail(String email);
    UserResponse findByUsername(String username);
    void deleteByUsername(String username);
    void updateUserProfile(String username ,  UserUpdateProfileRequest request);
    UserResponse updateUser (UserUpdateRequest request);
    void changePassword (UUID id , String oldPassword, String newPassword);
}
