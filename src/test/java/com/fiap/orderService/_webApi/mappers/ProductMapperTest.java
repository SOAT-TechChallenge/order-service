package com.fiap.orderService._webApi.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService.core.application.dto.ProductDTO;
import com.fiap.orderService.core.domain.entities.Product;
import com.fiap.orderService.core.domain.enums.Category;

class ProductMapperTest {

    @Test
    void shouldMapDtoToDomainSuccessfully() {
        UUID id = UUID.randomUUID();
        BigDecimal price = new BigDecimal("25.50");
        ProductDTO dto = new ProductDTO(id, "X-Bacon", price, Category.LANCHE);

        Product result = ProductMapper.toDomain(dto);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(dto.name(), result.getName());
        assertEquals(dto.price(), result.getPrice());
        assertEquals(dto.category(), result.getCategory());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        Product result = ProductMapper.toDomain(null);

        assertNull(result);
    }
}