package com.fiap.orderService.core.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.OrderStatus;

import lombok.Builder;

@Builder
public record OrderStatusHistoryDTO(
        UUID attendantId,
        OrderStatus status,
        LocalDateTime date
        ) {

}
