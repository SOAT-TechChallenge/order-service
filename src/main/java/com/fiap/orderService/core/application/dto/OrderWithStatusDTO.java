package com.fiap.orderService.core.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.OrderStatus;

import lombok.Builder;

@Builder
public record OrderWithStatusDTO(
        UUID id,
        OrderStatus status,
        LocalDateTime statusDt,
        UUID customerId,
        String customerName,
        UUID attendantId,
        String attendantName,
        BigDecimal price,
        LocalDateTime orderDt
        ) {

}
