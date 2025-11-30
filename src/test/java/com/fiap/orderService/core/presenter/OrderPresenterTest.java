package com.fiap.orderService.core.presenter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService.core.application.dto.OrderResponseDTO;
import com.fiap.orderService.core.application.dto.OrderStatusViewDTO;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesDTO;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.entities.OrderStatusHistory;

class OrderPresenterTest {

    @Test
    void shouldMapOrderToResponseDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal price = new BigDecimal("99.90");
        LocalDateTime date = LocalDateTime.now();
        String paymentId = "PAY-123";
        List<OrderItem> items = Collections.emptyList();
        List<OrderStatusHistory> history = Collections.emptyList();

        Order order = mock(Order.class);
        when(order.getId()).thenReturn(id);
        when(order.getItems()).thenReturn(items);
        when(order.getStatusHistory()).thenReturn(history);
        when(order.getCustomerId()).thenReturn(customerId);
        when(order.getPrice()).thenReturn(price);
        when(order.getDate()).thenReturn(date);
        when(order.getPaymentId()).thenReturn(paymentId);

        // Act
        OrderResponseDTO result = OrderPresenter.toDTO(order);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals(items, result.items());
        assertEquals(history, result.statusHistory());
        assertEquals(customerId, result.customerId());
        assertEquals(price, result.price());
        assertEquals(date, result.date());
        assertEquals(paymentId, result.paymentId());
    }

    @Test
    void shouldMapOrderWithStatusAndWaitMinutesDTOToStatusViewDTOList() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String status = "PRONTO";
        LocalDateTime now = LocalDateTime.now();
        UUID customerId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        int waitTime = 15;

        OrderWithStatusAndWaitMinutesDTO inputDto = OrderWithStatusAndWaitMinutesDTO.builder()
                .orderId(orderId)
                .status(status)
                .statusDt(now)
                .customerId(customerId)
                .attendantId(attendantId)
                .orderDt(now)
                .waitTimeMinutes(waitTime)
                .build();

        List<OrderWithStatusAndWaitMinutesDTO> inputList = Collections.singletonList(inputDto);

        // Act
        List<OrderStatusViewDTO> resultList = OrderPresenter.toStatusViewDTOList(inputList);

        // Assert
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        
        OrderStatusViewDTO result = resultList.get(0);
        assertEquals(orderId, result.orderId());
        assertEquals(status, result.status());
        assertEquals(now, result.statusDt());
        assertEquals(customerId, result.customerId());
        assertEquals(attendantId, result.attendantId());
        assertEquals(now, result.orderDt());
        assertEquals(waitTime, result.waitTimeMinutes());
    }
}