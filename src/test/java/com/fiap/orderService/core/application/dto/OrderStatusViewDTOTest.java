package com.fiap.orderService.core.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class OrderStatusViewDTOTest {

    @Test
    void shouldCreateDTOUsingBuilder() {
        UUID orderId = UUID.randomUUID();
        String status = "PRONTO";
        LocalDateTime now = LocalDateTime.now();
        UUID customerId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        int waitTimeMinutes = 20;

        OrderStatusViewDTO dto = OrderStatusViewDTO.builder()
                .orderId(orderId)
                .status(status)
                .statusDt(now)
                .customerId(customerId)
                .attendantId(attendantId)
                .orderDt(now)
                .waitTimeMinutes(waitTimeMinutes)
                .build();

        assertNotNull(dto);
        assertEquals(orderId, dto.orderId());
        assertEquals(status, dto.status());
        assertEquals(now, dto.statusDt());
        assertEquals(customerId, dto.customerId());
        assertEquals(attendantId, dto.attendantId());
        assertEquals(now, dto.orderDt());
        assertEquals(waitTimeMinutes, dto.waitTimeMinutes());
    }

    @Test
    void shouldCreateDTOUsingConstructor() {
        UUID orderId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        OrderStatusViewDTO dto = new OrderStatusViewDTO(
                orderId,
                "RECEBIDO",
                now,
                null,
                null,
                now,
                5
        );

        assertNotNull(dto);
        assertEquals(orderId, dto.orderId());
        assertEquals("RECEBIDO", dto.status());
        assertEquals(5, dto.waitTimeMinutes());
    }
}