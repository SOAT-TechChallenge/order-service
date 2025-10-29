package com.fiap.orderService.core.presenter;

import com.fiap.orderService.core.application.dto.OrderResponseDTO;
import com.fiap.orderService.core.domain.entities.Order;

public class OrderPresenter {

    public static OrderResponseDTO toDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .items(order.getItems())
                .statusHistory(order.getStatusHistory())
                .customerId(order.getCustomerId())
                .price(order.getPrice())
                .date(order.getDate())
                .paymentId(order.getPaymentId())
                .build();
    }

}
