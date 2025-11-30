package com.fiap.orderService.core.application.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.fiap.orderService.core.application.dto.UpdateOrderStatusInputDTO;
import com.fiap.orderService.core.application.services.OrderStatusUpdaterService;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.enums.OrderStatus;

@ExtendWith(MockitoExtension.class)
class UpdateOrderStatusUseCaseTest {

    @Mock
    private OrderStatusUpdaterService orderStatusUpdaterService;

    @InjectMocks
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Test
    void shouldMoveStatusToPaidSuccessfully() {
        UUID orderId = UUID.randomUUID();

        updateOrderStatusUseCase.execute(orderId);

        verify(orderStatusUpdaterService).moveStatusToPaid(orderId);
    }

    @Test
    void shouldMoveStatusToReceived() {
        UUID orderId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        UpdateOrderStatusInputDTO dto = new UpdateOrderStatusInputDTO(orderId, OrderStatus.RECEBIDO, attendantId);
        Order expectedOrder = mock(Order.class);

        when(orderStatusUpdaterService.moveStatusToReceived(orderId, attendantId)).thenReturn(expectedOrder);

        Order result = updateOrderStatusUseCase.execute(dto);

        assertEquals(expectedOrder, result);
        verify(orderStatusUpdaterService).moveStatusToReceived(orderId, attendantId);
    }

    @Test
    void shouldMoveStatusToInPreparation() {
        UUID orderId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        UpdateOrderStatusInputDTO dto = new UpdateOrderStatusInputDTO(orderId, OrderStatus.EM_PREPARACAO, attendantId);
        Order expectedOrder = mock(Order.class);

        when(orderStatusUpdaterService.moveStatusToInPreparation(orderId, attendantId)).thenReturn(expectedOrder);

        Order result = updateOrderStatusUseCase.execute(dto);

        assertEquals(expectedOrder, result);
        verify(orderStatusUpdaterService).moveStatusToInPreparation(orderId, attendantId);
    }

    @Test
    void shouldMoveStatusToReady() {
        UUID orderId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        UpdateOrderStatusInputDTO dto = new UpdateOrderStatusInputDTO(orderId, OrderStatus.PRONTO, attendantId);
        Order expectedOrder = mock(Order.class);

        when(orderStatusUpdaterService.moveStatusToReady(orderId, attendantId)).thenReturn(expectedOrder);

        Order result = updateOrderStatusUseCase.execute(dto);

        assertEquals(expectedOrder, result);
        verify(orderStatusUpdaterService).moveStatusToReady(orderId, attendantId);
    }

    @Test
    void shouldMoveStatusToFinished() {
        UUID orderId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        UpdateOrderStatusInputDTO dto = new UpdateOrderStatusInputDTO(orderId, OrderStatus.FINALIZADO, attendantId);
        Order expectedOrder = mock(Order.class);

        when(orderStatusUpdaterService.moveStatusToFinished(orderId, attendantId)).thenReturn(expectedOrder);

        Order result = updateOrderStatusUseCase.execute(dto);

        assertEquals(expectedOrder, result);
        verify(orderStatusUpdaterService).moveStatusToFinished(orderId, attendantId);
    }

    @Test
    void shouldMoveStatusToCanceled() {
        UUID orderId = UUID.randomUUID();
        UUID attendantId = UUID.randomUUID();
        UpdateOrderStatusInputDTO dto = new UpdateOrderStatusInputDTO(orderId, OrderStatus.CANCELADO, attendantId);
        Order expectedOrder = mock(Order.class);

        when(orderStatusUpdaterService.moveStatusToCanceled(orderId, attendantId)).thenReturn(expectedOrder);

        Order result = updateOrderStatusUseCase.execute(dto);

        assertEquals(expectedOrder, result);
        verify(orderStatusUpdaterService).moveStatusToCanceled(orderId, attendantId);
    }

    @Test
    void shouldThrowExceptionForInvalidStatusChange() {
        UUID orderId = UUID.randomUUID();
        // Usando um status que não está no switch case (ex: PAGO ou PAGAMENTO_PENDENTE)
        UpdateOrderStatusInputDTO dto = new UpdateOrderStatusInputDTO(orderId, OrderStatus.PAGO, null);

        assertThrows(IllegalArgumentException.class, () -> updateOrderStatusUseCase.execute(dto));
    }
}