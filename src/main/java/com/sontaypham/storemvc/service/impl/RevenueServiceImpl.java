package com.sontaypham.storemvc.service.impl;

import com.sontaypham.storemvc.repository.OrderRepository;
import com.sontaypham.storemvc.service.RevenueService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {
  private final OrderRepository orderRepository;

  @Override
  public BigDecimal getTodayRevenue() {
    return orderRepository.getTodayRevenue();
  }
}
