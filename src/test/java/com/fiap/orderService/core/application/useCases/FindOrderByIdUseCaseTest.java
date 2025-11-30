package com.fiap.orderService.core.application.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;
import com.fiap.orderService.core.gateways.order.OrderGateway;

@ExtendWith(MockitoExtension.class)
class FindOrderByIdUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private FindOrderByIdUseCase findOrderByIdUseCase;

    @Test
    void shouldReturnOrderWhenFound() {
        UUID id = UUID.randomUUID();
        Order expectedOrder = mock(Order.class);

        when(orderGateway.findById(id)).thenReturn(expectedOrder);

        Order result = findOrderByIdUseCase.execute(id);

        assertNotNull(result);
        assertEquals(expectedOrder, result);
        verify(orderGateway).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        UUID id = UUID.randomUUID();

        when(orderGateway.findById(id)).thenThrow(new EntityNotFoundException("Order not found"));

        assertThrows(EntityNotFoundException.class, () -> findOrderByIdUseCase.execute(id));
        verify(orderGateway).findById(id);
    }
}