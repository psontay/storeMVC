package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.dto.response.order.OrderStats;
import com.sontaypham.storemvc.dto.response.user.ProfileResponse;
import com.sontaypham.storemvc.enums.ErrorCode;
import com.sontaypham.storemvc.exception.ApiException;
import com.sontaypham.storemvc.model.User;
import com.sontaypham.storemvc.repository.OrderRepository;
import com.sontaypham.storemvc.repository.UserRepository;
import com.sontaypham.storemvc.service.ProfileService;
import com.sontaypham.storemvc.util.SecurityUtilStatic;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileServiceImpl implements ProfileService {
  OrderRepository orderRepository;
  UserRepository userRepository;

  @Override
  public ProfileResponse getCurrentProfile() {
      UUID userId = SecurityUtilStatic.getUserId();
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    boolean hasPassword = user.getPassword() != null && !user.getPassword().isEmpty();

    OrderStats orderStats = orderRepository.getOrderStatsByUserId(user.getId());
    long totalOrders = (orderStats != null)  ? orderStats.totalOrders() : 0;
    long pendingOrders =  (orderStats != null ) ? orderStats.pendingOrders() : 0;
    long deliveredOrders =  (orderStats != null ) ? orderStats.deliveredOrders() : 0;
    return ProfileResponse.builder()
        .username(user.getUsername())
         .hasPassword(hasPassword)
        .fullName(user.getFullName())
        .email(user.getEmail())
        .telPhone(user.getTelPhone())
        .address(user.getAddress())
        .totalOrders(totalOrders)
        .pendingOrders(pendingOrders)
        .deliveredOrders(deliveredOrders)
        .build();
  }
}
