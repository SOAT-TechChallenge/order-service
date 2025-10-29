package com.fiap.orderService.core.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.Category;

import lombok.Builder;

@Builder
public record ProductDTO(
        UUID id,
        String name,
        BigDecimal price,
        Category category
        ) {

}
