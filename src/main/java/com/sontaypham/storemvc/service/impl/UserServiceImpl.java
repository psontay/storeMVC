package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.request.user.UserCreationRequest;
import com.sontaypham.storemvc.dto.request.user.UserRegisterRequest;
import com.sontaypham.storemvc.dto.request.user.UserUpdateProfileRequest;
import com.sontaypham.storemvc.dto.response.user.UserCreationResponse;
import com.sontaypham.storemvc.dto.response.user.UserRegisterResponse;
import com.sontaypham.storemvc.dto.response.user.UserResponse;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.service.UserService;

import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        return null;
    }

    @Override
    public UserCreationResponse createUser(UserCreationRequest request) {
        return null;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void deleteByUsername(String username) {

    }

    @Override
    public void updateUserProfile(UserUpdateProfileRequest request) {

    }

    @Override
    public void changePassword(UUID id, String oldPassword, String newPassword) {

    }
}
