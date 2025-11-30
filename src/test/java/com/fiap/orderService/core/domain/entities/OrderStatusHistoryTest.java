package com.fiap.orderService.core.domain.entities;

import com.fiap.orderService.core.domain.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusHistoryTest {

    private static final UUID VALID_ATTENDANT_ID = UUID.randomUUID();
    private static final OrderStatus VALID_STATUS = OrderStatus.PAGO;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now();

    @Test
    void testOrderStatusHistoryBuild_SuccessfulCreation() {
        OrderStatusHistory history = OrderStatusHistory.build(VALID_ATTENDANT_ID, VALID_STATUS, VALID_DATE);

        assertNotNull(history);
        assertEquals(VALID_ATTENDANT_ID, history.getAttendantId());
        assertEquals(VALID_STATUS, history.getStatus());
        assertEquals(VALID_DATE, history.getDate());
    }

    @Test
    void testGetters() {
        UUID attendantId = UUID.randomUUID();
        OrderStatus status = OrderStatus.PRONTO;
        LocalDateTime date = LocalDateTime.now().minusMinutes(5);

        OrderStatusHistory history = OrderStatusHistory.build(attendantId, status, date);

        assertEquals(attendantId, history.getAttendantId());
        assertEquals(status, history.getStatus());
        assertEquals(date, history.getDate());
    }

    @Test
    void testOrderStatusHistoryBuild_NullStatus_ThrowsException() {
        OrderStatus status = null;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderStatusHistory.build(VALID_ATTENDANT_ID, status, VALID_DATE);
        });

        assertEquals("O status deve ser preenchido", thrown.getMessage());
    }

    @Test
    void testOrderStatusHistoryBuild_NullDate_ThrowsException() {
        LocalDateTime date = null;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderStatusHistory.build(VALID_ATTENDANT_ID, VALID_STATUS, date);
        });

        assertEquals("A data deve ser preenchida", thrown.getMessage());
    }

    @Test
    void testOrderStatusHistoryBuild_NullAttendantId_SuccessfulCreation() {
        UUID attendantId = null;

        OrderStatusHistory history = OrderStatusHistory.build(attendantId, VALID_STATUS, VALID_DATE);

        assertNotNull(history);
        assertNull(history.getAttendantId());
        assertEquals(VALID_STATUS, history.getStatus());
        assertEquals(VALID_DATE, history.getDate());
    }

    @Test
    void testDefaultConstructor() {
        OrderStatusHistory history = new OrderStatusHistory();

        assertNotNull(history);
        assertNull(history.getAttendantId());
        assertNull(history.getStatus());
        assertNull(history.getDate());
    }
}