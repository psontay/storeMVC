package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.repository.OrderRepository;
import com.sontaypham.storemvc.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {
    private final OrderRepository orderRepository;

    @Override
    public BigDecimal getTodayRevenue() {
        return orderRepository.getTodayRevenue();
    }
}
