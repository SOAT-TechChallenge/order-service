package com.fiap.orderService._webApi.data.persistence.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderStatusDocument {

    private UUID attendantId;

    private String status;

    private LocalDateTime date;

    public OrderStatusDocument() {
    }

    public OrderStatusDocument(UUID attendantId, String status, LocalDateTime date) {
        this.attendantId = attendantId;
        this.status = status;
        this.date = date;
    }

    public UUID getAttendantId() {
        return this.attendantId;
    }

    public void setAttendantId(UUID attendantId) {
        this.attendantId = attendantId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
