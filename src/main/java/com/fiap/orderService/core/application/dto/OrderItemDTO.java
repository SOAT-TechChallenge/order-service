package com.fiap.orderService.core.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.Category;

import lombok.Builder;

@Builder
public record OrderItemDTO(
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        Category category
        ) {

}
