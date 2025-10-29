package com.fiap.orderService.core.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.OrderStatus;

import lombok.Builder;

@Builder
public record OrderDetailsDTO(
        UUID orderId,
        OrderStatus status,
        LocalDateTime statusDt,
        UUID attendantId,
        String attendantName,
        UUID customerId,
        String customerName,
        BigDecimal price,
        LocalDateTime orderDt,
        String paymentId,
        List<OrderItemDTO> items
        ) {

}
