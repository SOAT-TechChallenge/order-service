package com.fiap.orderService.core.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.OrderStatus;

public class OrderStatusHistory {

    private UUID attendantId;

    private OrderStatus status;

    private LocalDateTime date;

    public OrderStatusHistory() {

    }

    private OrderStatusHistory(UUID attendantId, OrderStatus status, LocalDateTime date) {
        this.attendantId = attendantId;
        this.status = status;
        this.date = date;
    }

    public UUID getAttendantId() {
        return this.attendantId;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public static OrderStatusHistory build(UUID attendantId, OrderStatus status, LocalDateTime date) {
        validate(status, date);
        return new OrderStatusHistory(attendantId, status, date);
    }

    private static void validate(OrderStatus status, LocalDateTime date) {
        if (status == null) {
            throw new IllegalArgumentException("O status deve ser preenchido");
        }
        if (date == null) {
            throw new IllegalArgumentException("A data deve ser preenchida");
        }
    }

}
