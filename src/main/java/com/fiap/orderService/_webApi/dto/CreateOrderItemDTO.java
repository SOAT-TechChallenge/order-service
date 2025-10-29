package com.fiap.orderService._webApi.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CreateOrderItemDTO(
        UUID productId,
        Integer quantity
        ) {

}
