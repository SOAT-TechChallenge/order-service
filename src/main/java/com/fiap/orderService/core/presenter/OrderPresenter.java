package com.fiap.orderService.core.presenter;

import java.util.List;
import java.util.stream.Collectors;

import com.fiap.orderService.core.application.dto.OrderResponseDTO;
import com.fiap.orderService.core.application.dto.OrderStatusViewDTO;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesDTO;
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

    public static List<OrderStatusViewDTO> toStatusViewDTOList(List<OrderWithStatusAndWaitMinutesDTO> orders) {
        return orders.stream().map(OrderPresenter::toStatusViewDTO).collect(Collectors.toList());
    }

    private static OrderStatusViewDTO toStatusViewDTO(OrderWithStatusAndWaitMinutesDTO orderWithStatusAndWaitMinutesDTO) {
        return OrderStatusViewDTO.builder()
                .orderId(orderWithStatusAndWaitMinutesDTO.orderId())
                .status(orderWithStatusAndWaitMinutesDTO.status())
                .statusDt(orderWithStatusAndWaitMinutesDTO.statusDt())
                .customerId(orderWithStatusAndWaitMinutesDTO.customerId())
                .attendantId(orderWithStatusAndWaitMinutesDTO.attendantId())
                .orderDt(orderWithStatusAndWaitMinutesDTO.orderDt())
                .waitTimeMinutes(orderWithStatusAndWaitMinutesDTO.waitTimeMinutes())
                .build();
    }

}
