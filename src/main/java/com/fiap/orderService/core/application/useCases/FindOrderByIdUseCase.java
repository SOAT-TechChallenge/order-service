package com.fiap.orderService.core.application.useCases;

import java.util.UUID;

import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.gateways.order.OrderGateway;


public class FindOrderByIdUseCase {

    private final OrderGateway orderGateway;

    public FindOrderByIdUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public Order execute(UUID id) {
        return orderGateway.findById(id);
    }
}
