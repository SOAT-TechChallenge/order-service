package com.fiap.orderService.core.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.domain.exceptions.InvalidOrderStatusException;

class OrderTest {

    private static final UUID VALID_ID = UUID.randomUUID();
    private static final UUID VALID_CUSTOMER_ID = UUID.randomUUID();
    private static final String VALID_CUSTOMER_EMAIL = "test@test.com";
    private static final LocalDateTime VALID_DATE = LocalDateTime.now();

    @Test
    void shouldCreateOrderSuccessfully() {
        List<OrderItem> items = createValidItems();
        List<OrderStatusHistory> history = createValidHistory(OrderStatus.PAGAMENTO_PENDENTE);

        Order order = Order.build(VALID_ID, items, history, VALID_CUSTOMER_ID, VALID_CUSTOMER_EMAIL, VALID_DATE, null);

        assertNotNull(order);
        assertEquals(VALID_ID, order.getId());
        assertEquals(VALID_CUSTOMER_ID, order.getCustomerId());
        assertEquals(OrderStatus.PAGAMENTO_PENDENTE, order.getCurrentStatus());
    }

    @Test
    void shouldThrowExceptionWhenItemsAreNull() {
        List<OrderStatusHistory> history = createValidHistory(OrderStatus.PAGAMENTO_PENDENTE);

        assertThrows(IllegalArgumentException.class, () -> 
            Order.build(VALID_ID, null, history, VALID_CUSTOMER_ID, VALID_CUSTOMER_EMAIL, VALID_DATE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenItemsAreEmpty() {
        List<OrderItem> emptyItems = new ArrayList<>();
        List<OrderStatusHistory> history = createValidHistory(OrderStatus.PAGAMENTO_PENDENTE);

        assertThrows(IllegalArgumentException.class, () -> 
            Order.build(VALID_ID, emptyItems, history, VALID_CUSTOMER_ID, VALID_CUSTOMER_EMAIL, VALID_DATE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenHistoryIsNull() {
        List<OrderItem> items = createValidItems();

        assertThrows(IllegalArgumentException.class, () -> 
            Order.build(VALID_ID, items, null, VALID_CUSTOMER_ID, VALID_CUSTOMER_EMAIL, VALID_DATE, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenHistoryIsEmpty() {
        List<OrderItem> items = createValidItems();
        List<OrderStatusHistory> emptyHistory = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> 
            Order.build(VALID_ID, items, emptyHistory, VALID_CUSTOMER_ID, VALID_CUSTOMER_EMAIL, VALID_DATE, null)
        );
    }

    @Test
    void shouldCalculateTotalPriceCorrectly() {
        OrderItem item1 = OrderItem.build(UUID.randomUUID(), "Item 1", 2, new BigDecimal("10.00"), Category.LANCHE);
        OrderItem item2 = OrderItem.build(UUID.randomUUID(), "Item 2", 1, new BigDecimal("5.50"), Category.BEBIDA);
        List<OrderItem> items = Arrays.asList(item1, item2);
        List<OrderStatusHistory> history = createValidHistory(OrderStatus.PAGAMENTO_PENDENTE);
        
        Order order = Order.build(VALID_ID, items, history, VALID_CUSTOMER_ID, VALID_CUSTOMER_EMAIL, VALID_DATE, null);

        assertEquals(new BigDecimal("25.50"), order.getPrice());
    }

    @Test
    void shouldReturnLatestStatusAsCurrent() {
        List<OrderItem> items = createValidItems();
        List<OrderStatusHistory> history = new ArrayList<>();
        history.add(OrderStatusHistory.build(null, OrderStatus.RECEBIDO, LocalDateTime.now().minusHours(2)));
        history.add(OrderStatusHistory.build(null, OrderStatus.EM_PREPARACAO, LocalDateTime.now()));

        Order order = Order.build(VALID_ID, items, history, VALID_CUSTOMER_ID, VALID_CUSTOMER_EMAIL, VALID_DATE, null);

        assertEquals(OrderStatus.EM_PREPARACAO, order.getCurrentStatus());
    }

    @Test
    void shouldMoveStatusToPaidSuccessfully() {
        Order order = createOrderWithStatus(OrderStatus.PAGAMENTO_PENDENTE);
        
        order.moveStatusToPaid();
        System.out.println("Status Atual: " + order.getCurrentStatus());
        System.out.println("Histórico:");
        order.getStatusHistory().forEach(h -> 
            System.out.println(" - " + h.getStatus() + " em " + h.getDate())
        );

        assertEquals(OrderStatus.PAGO, order.getCurrentStatus());
    }

    @Test
    void shouldThrowExceptionWhenMovingToPaidFromInvalidStatus() {
        Order order = createOrderWithStatus(OrderStatus.RECEBIDO);

        assertThrows(InvalidOrderStatusException.class, () -> order.moveStatusToPaid());
    }

    @Test
    void shouldMoveStatusToReceivedSuccessfully() {
        Order order = createOrderWithStatus(OrderStatus.PAGO);
        UUID attendantId = UUID.randomUUID();

        order.moveStatusToReceived(attendantId);

        assertEquals(OrderStatus.RECEBIDO, order.getCurrentStatus());
    }

    @Test
    void shouldMoveStatusToInPreparationSuccessfully() {
        Order order = createOrderWithStatus(OrderStatus.RECEBIDO);
        UUID attendantId = UUID.randomUUID();

        order.moveStatusToInPreparation(attendantId);

        assertEquals(OrderStatus.EM_PREPARACAO, order.getCurrentStatus());
    }

    @Test
    void shouldThrowExceptionWhenMovingToInPreparationFromInvalidStatus() {
        Order order = createOrderWithStatus(OrderStatus.PAGO);
        UUID attendantId = UUID.randomUUID();

        assertThrows(InvalidOrderStatusException.class, () -> order.moveStatusToInPreparation(attendantId));
    }

    @Test
    void shouldMoveStatusToReadySuccessfully() {
        Order order = createOrderWithStatus(OrderStatus.EM_PREPARACAO);
        UUID attendantId = UUID.randomUUID();

        order.moveStatusToReady(attendantId);

        assertEquals(OrderStatus.PRONTO, order.getCurrentStatus());
    }

    @Test
    void shouldMoveStatusToFinishedSuccessfully() {
        Order order = createOrderWithStatus(OrderStatus.PRONTO);
        UUID attendantId = UUID.randomUUID();

        order.moveStatusToFinished(attendantId);

        assertEquals(OrderStatus.FINALIZADO, order.getCurrentStatus());
    }

    @Test
    void shouldMoveStatusToCanceledSuccessfully() {
        Order order = createOrderWithStatus(OrderStatus.PAGAMENTO_PENDENTE);
        UUID attendantId = UUID.randomUUID();

        order.moveStatusToCanceled(attendantId);

        assertEquals(OrderStatus.CANCELADO, order.getCurrentStatus());
    }

    @Test
    void shouldThrowExceptionWhenChangingStatusOfCanceledOrder() {
        Order order = createOrderWithStatus(OrderStatus.CANCELADO);

        InvalidOrderStatusException exception = assertThrows(InvalidOrderStatusException.class, 
            () -> order.moveStatusToPaid()
        );
        assertTrue(exception.getMessage().contains("Não é possível alterar status de um pedido que já foi finalizado ou cancelado."));
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateStatus() {
        Order order = createOrderWithStatus(OrderStatus.PAGAMENTO_PENDENTE);
        
        order.moveStatusToPaid();

        assertThrows(InvalidOrderStatusException.class, () -> order.moveStatusToPaid());
    }

    private List<OrderItem> createValidItems() {
        return Collections.singletonList(
            OrderItem.build(UUID.randomUUID(), "Produto Teste", 1, BigDecimal.TEN, Category.LANCHE)
        );
    }

    private List<OrderStatusHistory> createValidHistory(OrderStatus status) {
        List<OrderStatusHistory> history = new ArrayList<>();
        history.add(OrderStatusHistory.build(null, status, LocalDateTime.now()));
        return history;
    }

    private Order createOrderWithStatus(OrderStatus status) {
        List<OrderItem> items = createValidItems();
        List<OrderStatusHistory> history = createValidHistory(status);
        return Order.build(VALID_ID, items, history, VALID_CUSTOMER_ID, VALID_CUSTOMER_EMAIL, VALID_DATE, null);
    }
}