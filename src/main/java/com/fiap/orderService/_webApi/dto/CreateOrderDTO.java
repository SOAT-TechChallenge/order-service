package com.fiap.orderService._webApi.dto;

import java.util.List;
import java.util.UUID;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateOrderDTO(
        @NotNull
        List<CreateOrderItemDTO> items,
        @NotNull
        UUID customerId,
        @NotNull
        String customerEmail
        ) {

}
