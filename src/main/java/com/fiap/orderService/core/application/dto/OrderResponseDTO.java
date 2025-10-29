package com.fiap.orderService.core.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.entities.OrderStatusHistory;

import lombok.Builder;

@Builder
public record OrderResponseDTO(
        UUID id,
        List<OrderItem> items,
        List<OrderStatusHistory> statusHistory,
        UUID customerId,
        BigDecimal price,
        LocalDateTime date,
        String paymentId
        ) {

}
