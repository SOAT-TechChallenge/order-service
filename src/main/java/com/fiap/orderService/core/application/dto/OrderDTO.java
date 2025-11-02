package com.fiap.orderService.core.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record OrderDTO(
        UUID id,
        List<OrderItemDTO> items,
        List<OrderStatusHistoryDTO> statusHistory,
        UUID customerId,
        String customerEmail,
        BigDecimal price,
        LocalDateTime date,
        String paymentId
        ) {

}
