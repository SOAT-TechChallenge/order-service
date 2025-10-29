package com.fiap.orderService._webApi.dto;

import java.util.UUID;

import com.fiap.orderService.core.domain.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateOrderStatusDTO(
        @NotNull
        UUID orderId,
        @NotNull
        OrderStatus status,
        UUID attendantId
        ) {

}
