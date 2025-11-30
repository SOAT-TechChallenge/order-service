package com.fiap.orderService.core.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService.core.domain.enums.OrderStatus;

class OrderSummaryDTOTest {

    @Test
    void shouldCreateDTOUsingBuilder() {
        UUID id = UUID.randomUUID();
        OrderStatus status = OrderStatus.PAGO;
        LocalDateTime now = LocalDateTime.now();
        UUID customerId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        BigDecimal price = new BigDecimal("100.00");

        OrderSummaryDTO dto = OrderSummaryDTO.builder()
                .id(id)
                .status(status)
                .statusDt(now)
                .customerId(customerId)
                .customerName("John Doe")
                .attendantId(attendantId)
                .attendantName("Attendant Jane")
                .price(price)
                .orderDt(now)
                .build();

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(status, dto.status());
        assertEquals(now, dto.statusDt());
        assertEquals(customerId, dto.customerId());
        assertEquals("John Doe", dto.customerName());
        assertEquals(attendantId, dto.attendantId());
        assertEquals("Attendant Jane", dto.attendantName());
        assertEquals(price, dto.price());
        assertEquals(now, dto.orderDt());
    }

    @Test
    void shouldCreateDTOUsingConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        OrderSummaryDTO dto = new OrderSummaryDTO(
                id,
                OrderStatus.CANCELADO,
                now,
                null,
                null,
                null,
                null,
                BigDecimal.ZERO,
                now
        );

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(OrderStatus.CANCELADO, dto.status());
        assertEquals(BigDecimal.ZERO, dto.price());
    }
}