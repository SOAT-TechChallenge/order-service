package com.fiap.orderService.core.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService.core.domain.enums.OrderStatus;

class UpdateOrderStatusInputDTOTest {

    @Test
    void shouldCreateDTOUsingBuilder() {
        UUID orderId = UUID.randomUUID();
        OrderStatus status = OrderStatus.EM_PREPARACAO;
        UUID attendantId = UUID.randomUUID();

        UpdateOrderStatusInputDTO dto = UpdateOrderStatusInputDTO.builder()
                .orderId(orderId)
                .status(status)
                .attendantId(attendantId)
                .build();

        assertNotNull(dto);
        assertEquals(orderId, dto.orderId());
        assertEquals(status, dto.status());
        assertEquals(attendantId, dto.attendantId());
    }

    @Test
    void shouldCreateDTOUsingConstructor() {
        UUID orderId = UUID.randomUUID();
        OrderStatus status = OrderStatus.FINALIZADO;

        UpdateOrderStatusInputDTO dto = new UpdateOrderStatusInputDTO(
                orderId,
                status,
                null
        );

        assertNotNull(dto);
        assertEquals(orderId, dto.orderId());
        assertEquals(status, dto.status());
        assertNull(dto.attendantId());
    }
}