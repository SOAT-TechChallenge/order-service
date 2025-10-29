package com.fiap.orderService._webApi.data.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "orders")
public class OrderDocument {

    @Id
    private UUID id;

    private UUID customerId;

    private String currentStatus;

    private LocalDateTime lastUpdateDate;

    private List<OrderItemDocument> items;

    private List<OrderStatusDocument> statusHistory;

    private BigDecimal price;

    private LocalDateTime date;

    private String paymentId;

    public OrderDocument() {
    }

    public OrderDocument(UUID id, UUID customerId, String currentStatus, LocalDateTime lastUpdateDate, List<OrderItemDocument> items, List<OrderStatusDocument> statusHistory, BigDecimal price, LocalDateTime date, String paymentId) {
        this.id = id;
        this.customerId = customerId;
        this.currentStatus = currentStatus;
        this.lastUpdateDate = lastUpdateDate;
        this.items = items;
        this.statusHistory = statusHistory;
        this.price = price;
        this.date = date;
        this.paymentId = paymentId;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCurrentStatus() {
        return this.currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public LocalDateTime getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<OrderItemDocument> getItems() {
        return this.items;
    }

    public void setItems(List<OrderItemDocument> items) {
        this.items = items;
    }

    public List<OrderStatusDocument> getStatusHistory() {
        return this.statusHistory;
    }

    public void setStatusHistory(List<OrderStatusDocument> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

}
