package com.fiap.orderService.core.gateways.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.domain.entities.Order;

public interface OrderGateway {

    Order save(Order order);

    Order findById(UUID id);

    Order findByPaymentId(String paymentId);

    List<Order> listByClientId(UUID clientId);

    List<Order> listByPeriod(LocalDateTime initialDt, LocalDateTime finalDt);

    List<Order> listTodayOrders(List<String> statusList, LocalDateTime initialDt, LocalDateTime finalDt);

}
