package com.fiap.orderService.core.gateways.order;

import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesDTO;
import com.fiap.orderService.core.domain.entities.Order;

public interface OrderGateway {

    Order save(Order order);

    Order findById(UUID id);

    Order findByPaymentId(String paymentId);

    List<OrderWithStatusAndWaitMinutesDTO> listTodayOrders(List<String> statusList, int finalizedMinutes);
}
