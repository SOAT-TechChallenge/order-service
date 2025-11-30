package com.fiap.orderService.core.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;

class OrderDetailsDTOTest {

    @Test
    void shouldCreateOrderDetailsDTOUsingBuilder() {
        UUID orderId = UUID.randomUUID();
        OrderStatus status = OrderStatus.PRONTO;
        LocalDateTime now = LocalDateTime.now();
        UUID attendantId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal price = new BigDecimal("50.00");
        List<OrderItemDTO> items = Collections.singletonList(
            new OrderItemDTO(UUID.randomUUID(), "Item 1", 1, BigDecimal.TEN, Category.LANCHE)
        );

        OrderDetailsDTO dto = OrderDetailsDTO.builder()
                .orderId(orderId)
                .status(status)
                .statusDt(now)
                .attendantId(attendantId)
                .attendantName("Attendant Name")
                .customerId(customerId)
                .customerName("Customer Name")
                .price(price)
                .orderDt(now)
                .paymentId("PAY-123")
                .items(items)
                .build();

        assertNotNull(dto);
        assertEquals(orderId, dto.orderId());
        assertEquals(status, dto.status());
        assertEquals(now, dto.statusDt());
        assertEquals(attendantId, dto.attendantId());
        assertEquals("Attendant Name", dto.attendantName());
        assertEquals(customerId, dto.customerId());
        assertEquals("Customer Name", dto.customerName());
        assertEquals(price, dto.price());
        assertEquals(now, dto.orderDt());
        assertEquals("PAY-123", dto.paymentId());
        assertEquals(1, dto.items().size());
    }

    @Test
    void shouldCreateOrderDetailsDTOUsingConstructor() {
        UUID orderId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        OrderDetailsDTO dto = new OrderDetailsDTO(
                orderId,
                OrderStatus.RECEBIDO,
                now,
                null,
                null,
                null,
                null,
                BigDecimal.ZERO,
                now,
                null,
                Collections.emptyList()
        );

        assertNotNull(dto);
        assertEquals(orderId, dto.orderId());
        assertEquals(OrderStatus.RECEBIDO, dto.status());
        assertTrue(dto.items().isEmpty());
    }
}