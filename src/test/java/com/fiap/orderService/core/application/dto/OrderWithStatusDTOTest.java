package com.fiap.orderService.core.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService.core.domain.enums.OrderStatus;

class OrderWithStatusDTOTest {

    @Test
    void shouldCreateDTOUsingBuilder() {
        UUID id = UUID.randomUUID();
        OrderStatus status = OrderStatus.EM_PREPARACAO;
        LocalDateTime now = LocalDateTime.now();
        UUID customerId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        BigDecimal price = new BigDecimal("75.50");

        OrderWithStatusDTO dto = OrderWithStatusDTO.builder()
                .id(id)
                .status(status)
                .statusDt(now)
                .customerId(customerId)
                .customerName("Customer Name")
                .attendantId(attendantId)
                .attendantName("Attendant Name")
                .price(price)
                .orderDt(now)
                .build();

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(status, dto.status());
        assertEquals(now, dto.statusDt());
        assertEquals(customerId, dto.customerId());
        assertEquals("Customer Name", dto.customerName());
        assertEquals(attendantId, dto.attendantId());
        assertEquals("Attendant Name", dto.attendantName());
        assertEquals(price, dto.price());
        assertEquals(now, dto.orderDt());
    }

    @Test
    void shouldCreateDTOUsingConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        OrderWithStatusDTO dto = new OrderWithStatusDTO(
                id,
                OrderStatus.PRONTO,
                now,
                null,
                null,
                null,
                null,
                BigDecimal.TEN,
                now
        );

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(OrderStatus.PRONTO, dto.status());
        assertEquals(BigDecimal.TEN, dto.price());
    }
}