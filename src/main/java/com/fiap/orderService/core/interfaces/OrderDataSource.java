package com.fiap.orderService.core.interfaces;

import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.application.dto.OrderDTO;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesProjection;

public interface OrderDataSource {

    OrderDTO save(OrderDTO order);

    OrderDTO findById(UUID id);

    OrderDTO findByPaymentId(String paymentId);

    List<OrderDTO> listByClientId(UUID clientId);

    List<OrderWithStatusAndWaitMinutesProjection> listTodayOrders(List<String> statusList, int finalizedMinutes);
}
