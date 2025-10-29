package com.fiap.orderService.core.application.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CreateOrderItemInputDTO(
        UUID productId,
        Integer quantity
        ) {

}
