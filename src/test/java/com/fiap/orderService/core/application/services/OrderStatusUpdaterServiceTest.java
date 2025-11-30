package com.fiap.orderService.core.application.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.orderService.core.domain.entities.Attendant;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;
import com.fiap.orderService.core.gateways.notification.EmailNotificationGateway;
import com.fiap.orderService.core.gateways.order.OrderGateway;
import com.fiap.orderService.core.gateways.user.AttendantGateway;

@ExtendWith(MockitoExtension.class)
class OrderStatusUpdaterServiceTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private AttendantGateway attendantGateway;

    @Mock
    private EmailNotificationGateway emailGateway;

    @InjectMocks
    private OrderStatusUpdaterService service;

    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final UUID ATTENDANT_ID = UUID.randomUUID();
    private static final String CUSTOMER_EMAIL = "customer@test.com";

    @Test
    void shouldMoveStatusToPaidSuccessfully() {
        Order order = mock(Order.class);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);

        Order result = service.moveStatusToPaid(ORDER_ID);

        assertNotNull(result);
        verify(order).moveStatusToPaid();
        verify(orderGateway).save(order);
    }

    @Test
    void shouldMoveStatusToReceivedAndNotify() {
        Order order = mockOrderWithEmail(CUSTOMER_EMAIL);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);
        when(order.getCurrentStatus()).thenReturn(OrderStatus.RECEBIDO);
        
        // Simula atendente válido
        when(attendantGateway.findById(ATTENDANT_ID)).thenReturn(mock(Attendant.class));

        Order result = service.moveStatusToReceived(ORDER_ID, ATTENDANT_ID);

        assertNotNull(result);
        verify(order).moveStatusToReceived(ATTENDANT_ID);
        verify(emailGateway).sendEmail(eq(CUSTOMER_EMAIL), eq(ORDER_ID), eq(OrderStatus.RECEBIDO));
        verify(orderGateway).save(order);
    }

    @Test
    void shouldMoveStatusToInPreparationAndNotify() {
        Order order = mockOrderWithEmail(CUSTOMER_EMAIL);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);
        when(order.getCurrentStatus()).thenReturn(OrderStatus.EM_PREPARACAO);
        
        when(attendantGateway.findById(ATTENDANT_ID)).thenReturn(mock(Attendant.class));

        Order result = service.moveStatusToInPreparation(ORDER_ID, ATTENDANT_ID);

        verify(order).moveStatusToInPreparation(ATTENDANT_ID);
        verify(emailGateway).sendEmail(eq(CUSTOMER_EMAIL), eq(ORDER_ID), eq(OrderStatus.EM_PREPARACAO));
    }

    @Test
    void shouldMoveStatusToReadyAndNotify() {
        Order order = mockOrderWithEmail(CUSTOMER_EMAIL);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);
        when(order.getCurrentStatus()).thenReturn(OrderStatus.PRONTO);
        
        when(attendantGateway.findById(ATTENDANT_ID)).thenReturn(mock(Attendant.class));

        Order result = service.moveStatusToReady(ORDER_ID, ATTENDANT_ID);

        verify(order).moveStatusToReady(ATTENDANT_ID);
        verify(emailGateway).sendEmail(eq(CUSTOMER_EMAIL), eq(ORDER_ID), eq(OrderStatus.PRONTO));
    }

    @Test
    void shouldMoveStatusToFinishedAndNotify() {
        Order order = mockOrderWithEmail(CUSTOMER_EMAIL);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);
        when(order.getCurrentStatus()).thenReturn(OrderStatus.FINALIZADO);
        
        when(attendantGateway.findById(ATTENDANT_ID)).thenReturn(mock(Attendant.class));

        Order result = service.moveStatusToFinished(ORDER_ID, ATTENDANT_ID);

        verify(order).moveStatusToFinished(ATTENDANT_ID);
        verify(emailGateway).sendEmail(eq(CUSTOMER_EMAIL), eq(ORDER_ID), eq(OrderStatus.FINALIZADO));
    }

    @Test
    void shouldMoveStatusToCanceledWithoutNotification() {
        Order order = mock(Order.class);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);
        
        when(attendantGateway.findById(ATTENDANT_ID)).thenReturn(mock(Attendant.class));

        service.moveStatusToCanceled(ORDER_ID, ATTENDANT_ID);

        verify(order).moveStatusToCanceled(ATTENDANT_ID);
        verify(orderGateway).save(order);
        // Verifica que NÃO enviou email (conforme a implementação atual do service)
        verify(emailGateway, never()).sendEmail(any(), any(), any());
    }

    @Test
    void shouldThrowEntityNotFoundWhenOrderDoesNotExist() {
        when(orderGateway.findById(ORDER_ID)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> service.moveStatusToPaid(ORDER_ID));
    }

    @Test
    void shouldThrowEntityNotFoundWhenAttendantDoesNotExist() {
        when(attendantGateway.findById(ATTENDANT_ID)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, 
            () -> service.moveStatusToReceived(ORDER_ID, ATTENDANT_ID));
            
        verify(orderGateway, never()).findById(any()); // Falha antes de buscar o pedido no caso do Received
    }

    @Test
    void shouldHandleEmailNotificationErrorSilently() {
        Order order = mockOrderWithEmail(CUSTOMER_EMAIL);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);
        when(attendantGateway.findById(ATTENDANT_ID)).thenReturn(mock(Attendant.class));

        // Simula erro no envio de email
        doThrow(new RuntimeException("Email service down"))
            .when(emailGateway).sendEmail(any(), any(), any());

        // Não deve lançar exceção
        Order result = service.moveStatusToReceived(ORDER_ID, ATTENDANT_ID);

        assertNotNull(result);
        verify(orderGateway).save(order); // Garante que salvou mesmo com erro no email
    }

    @Test
    void shouldValidateAttendantWhenIdIsProvided() {
        // Testa o caminho feliz da validação privada via um método público
        Order order = mock(Order.class);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);
        when(attendantGateway.findById(ATTENDANT_ID)).thenReturn(mock(Attendant.class));
        
        service.moveStatusToFinished(ORDER_ID, ATTENDANT_ID);
        
        verify(attendantGateway).findById(ATTENDANT_ID);
    }
    
    @Test
    void shouldAllowNullAttendantIfValidationPermits() {
        // Nota: Baseado na implementação de `validatettendant`, se ID for null, retorna null sem erro.
        // Vamos testar um método que chama validatettendant, ex: moveStatusToReceived
        
        Order order = mockOrderWithEmail(CUSTOMER_EMAIL);
        when(orderGateway.findById(ORDER_ID)).thenReturn(order);
        when(orderGateway.save(order)).thenReturn(order);
        
        service.moveStatusToReceived(ORDER_ID, null);
        
        verify(attendantGateway, never()).findById(any());
        verify(order).moveStatusToReceived(null);
    }

    private Order mockOrderWithEmail(String email) {
        Order order = mock(Order.class);
        when(order.getCustomerEmail()).thenReturn(email);
        return order;
    }
}