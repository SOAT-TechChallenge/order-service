package com.fiap.orderService.core.application.useCases;

import java.util.List;

import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesDTO;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.gateways.order.OrderGateway;

public class ListTodayOrdersUseCase {

    private final OrderGateway orderGateway;

    public ListTodayOrdersUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public List<OrderWithStatusAndWaitMinutesDTO> execute() {
        List<String> statusList = List.of(
                OrderStatus.RECEBIDO.name(),
                OrderStatus.EM_PREPARACAO.name(),
                OrderStatus.PRONTO.name()
        );

        return orderGateway.listTodayOrders(statusList, 5);
    }
}
