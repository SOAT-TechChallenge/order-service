package com.fiap.orderService.core.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record OrderStatusViewDTO(
        UUID orderId,
        String status,
        LocalDateTime statusDt,
        UUID customerId,
        UUID attendantId,
        LocalDateTime orderDt,
        int waitTimeMinutes
        ) {

}
