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


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class ProfileServiceImpl implements ProfileService {
    OrderRepository orderRepository;
    UserRepository userRepository;
    @Override
    public ProfileResponse getCurrentProfile() {
        User user = userRepository.findById(SecurityUtilStatic.getUserId()).orElseThrow(() -> new ApiException(
                ErrorCode.USER_NOT_FOUND));
        OrderStats orderStats = orderRepository.getOrderStatsByUserId(user.getId());
        long totalOrders = orderStats.totalOrders();
        long pendingOrders = orderStats.pendingOrders();
        long deliveredOrders = orderStats.deliveredOrders();
        return ProfileResponse.builder()
                              .username(user.getUsername())
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
