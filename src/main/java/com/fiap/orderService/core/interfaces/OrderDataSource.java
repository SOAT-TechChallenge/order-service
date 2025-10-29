package com.fiap.orderService.core.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.application.dto.OrderDTO;

public interface OrderDataSource {

    OrderDTO save(OrderDTO order);

    OrderDTO findById(UUID id);

    OrderDTO findByPaymentId(String paymentId);

    List<OrderDTO> listByClientId(UUID clientId);

    List<OrderDTO> listByPeriod(LocalDateTime initialDt, LocalDateTime finalDt);

    List<OrderDTO> listTodayOrders(List<String> statusList, LocalDateTime initialDt, LocalDateTime finalDt);
}
