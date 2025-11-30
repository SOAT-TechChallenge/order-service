package com.fiap.orderService.core.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.entities.OrderStatusHistory;

class OrderResponseDTOTest {

    @Test
    void shouldCreateDTOUsingBuilder() {
        UUID id = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal price = new BigDecimal("120.50");
        LocalDateTime now = LocalDateTime.now();
        String paymentId = "PAY-001";
        // Usando listas vazias para simplificar, já que o foco é o DTO
        List<OrderItem> items = Collections.emptyList(); 
        List<OrderStatusHistory> history = Collections.emptyList();

        OrderResponseDTO dto = OrderResponseDTO.builder()
                .id(id)
                .items(items)
                .statusHistory(history)
                .customerId(customerId)
                .price(price)
                .date(now)
                .paymentId(paymentId)
                .build();

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(items, dto.items());
        assertEquals(history, dto.statusHistory());
        assertEquals(customerId, dto.customerId());
        assertEquals(price, dto.price());
        assertEquals(now, dto.date());
        assertEquals(paymentId, dto.paymentId());
    }

    @Test
    void shouldCreateDTOUsingConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        OrderResponseDTO dto = new OrderResponseDTO(
                id,
                null,
                null,
                null,
                BigDecimal.ZERO,
                now,
                null
        );

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(BigDecimal.ZERO, dto.price());
        assertEquals(now, dto.date());
    }
}