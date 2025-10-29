package com.fiap.orderService.core.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.OrderStatus;

import lombok.Builder;

@Builder
public record OrderStatusViewDTO(
        UUID orderId,
        OrderStatus status,
        LocalDateTime statusDt,
        UUID customerId,
        String customerName,
        UUID attendantId,
        String attendantName,
        LocalDateTime orderDt,
        int waitTimeMinutes
        ) {

}
