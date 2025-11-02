package com.fiap.orderService.core.application.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateOrderInputDTO(
        @NotNull
        List<CreateOrderItemInputDTO> items,
        @NotNull
        UUID customerId,
        @NotNull
        String customerEmail
        ) {

}
