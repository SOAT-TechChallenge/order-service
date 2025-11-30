package com.fiap.orderService.core.gateways.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.orderService.core.application.dto.OrderDTO;
import com.fiap.orderService.core.application.dto.OrderItemDTO;
import com.fiap.orderService.core.application.dto.OrderStatusHistoryDTO;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesDTO;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesProjection;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.entities.OrderStatusHistory;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.interfaces.OrderDataSource;

@ExtendWith(MockitoExtension.class)
class OrderGatewayImplTest {

    @Mock
    private OrderDataSource dataSource;

    @InjectMocks
    private OrderGatewayImpl gateway;

    @Test
    void shouldSaveOrderSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Order order = createValidOrder(id);
        OrderDTO returnDto = createValidOrderDTO(id);

        when(dataSource.save(any(OrderDTO.class))).thenReturn(returnDto);

        // Act
        Order result = gateway.save(order);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(dataSource).save(any(OrderDTO.class));
    }

    @Test
    void shouldFindByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        OrderDTO returnDto = createValidOrderDTO(id);

        when(dataSource.findById(id)).thenReturn(returnDto);

        Order result = gateway.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldReturnNullWhenFindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(dataSource.findById(id)).thenReturn(null);

        Order result = gateway.findById(id);

        assertNull(result);
    }

    @Test
    void shouldFindByPaymentIdSuccessfully() {
        String paymentId = "PAY-123";
        UUID id = UUID.randomUUID();
        OrderDTO returnDto = createValidOrderDTO(id);

        when(dataSource.findByPaymentId(paymentId)).thenReturn(returnDto);

        Order result = gateway.findByPaymentId(paymentId);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldReturnNullWhenFindByPaymentIdNotFound() {
        String paymentId = "PAY-MISSING";
        when(dataSource.findByPaymentId(paymentId)).thenReturn(null);

        Order result = gateway.findByPaymentId(paymentId);

        assertNull(result);
    }

    @Test
    void shouldListTodayOrdersSuccessfully() {
        List<String> statusList = Collections.singletonList("RECEBIDO");
        int minutes = 10;
        
        OrderWithStatusAndWaitMinutesProjection projection = mock(OrderWithStatusAndWaitMinutesProjection.class);
        when(projection.getOrderId()).thenReturn(UUID.randomUUID());
        when(projection.getStatus()).thenReturn("RECEBIDO");
        when(projection.getStatusDt()).thenReturn(LocalDateTime.now());
        
        List<OrderWithStatusAndWaitMinutesProjection> projections = Collections.singletonList(projection);

        when(dataSource.listTodayOrders(statusList, minutes)).thenReturn(projections);

        List<OrderWithStatusAndWaitMinutesDTO> result = gateway.listTodayOrders(statusList, minutes);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(dataSource).listTodayOrders(statusList, minutes);
    }

    private Order createValidOrder(UUID id) {
        OrderItem item = OrderItem.build(
            UUID.randomUUID(), "Burger", 1, BigDecimal.TEN, Category.LANCHE
        );
        OrderStatusHistory history = OrderStatusHistory.build(null, OrderStatus.RECEBIDO, LocalDateTime.now());

        return Order.build(
            id,
            Collections.singletonList(item),
            Collections.singletonList(history),
            UUID.randomUUID(),
            "test@test.com",
            LocalDateTime.now(),
            null
        );
    }

    private OrderDTO createValidOrderDTO(UUID id) {
        return OrderDTO.builder()
            .id(id)
            .customerId(UUID.randomUUID())
            .customerEmail("test@test.com")
            .date(LocalDateTime.now())
            .items(Collections.singletonList(
                new OrderItemDTO(UUID.randomUUID(), "Burger", 1, BigDecimal.TEN, Category.LANCHE)
            ))
            .statusHistory(Collections.singletonList(
                new OrderStatusHistoryDTO(null, OrderStatus.RECEBIDO, LocalDateTime.now())
            ))
            .price(BigDecimal.TEN)
            .build();
    }
}