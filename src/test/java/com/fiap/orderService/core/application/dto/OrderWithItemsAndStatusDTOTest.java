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

class OrderWithItemsAndStatusDTOTest {

    @Test
    void shouldCreateDTOUsingBuilder() {
        UUID orderId = UUID.randomUUID();
        OrderStatus status = OrderStatus.EM_PREPARACAO;
        LocalDateTime now = LocalDateTime.now();
        UUID attendantId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal price = new BigDecimal("99.90");
        List<OrderItemDTO> items = Collections.singletonList(
            new OrderItemDTO(UUID.randomUUID(), "Pizza", 1, price, Category.LANCHE)
        );

        OrderWithItemsAndStatusDTO dto = OrderWithItemsAndStatusDTO.builder()
                .orderId(orderId)
                .status(status)
                .statusDt(now)
                .attendantId(attendantId)
                .attendantName("Attendant Test")
                .customerId(customerId)
                .customerName("Customer Test")
                .price(price)
                .orderDt(now)
                .paymentId("PAY-XYZ")
                .items(items)
                .build();

        assertNotNull(dto);
        assertEquals(orderId, dto.orderId());
        assertEquals(status, dto.status());
        assertEquals(now, dto.statusDt());
        assertEquals(attendantId, dto.attendantId());
        assertEquals("Attendant Test", dto.attendantName());
        assertEquals(customerId, dto.customerId());
        assertEquals("Customer Test", dto.customerName());
        assertEquals(price, dto.price());
        assertEquals(now, dto.orderDt());
        assertEquals("PAY-XYZ", dto.paymentId());
        assertEquals(1, dto.items().size());
    }

    @Test
    void shouldCreateDTOUsingConstructor() {
        UUID orderId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        OrderWithItemsAndStatusDTO dto = new OrderWithItemsAndStatusDTO(
                orderId,
                OrderStatus.FINALIZADO,
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
        assertEquals(OrderStatus.FINALIZADO, dto.status());
        assertTrue(dto.items().isEmpty());
    }
}