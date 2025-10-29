package com.fiap.orderService._webApi.data.persistence.entities;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemDocument {

    private UUID productId;

    private String productName;

    private Integer quantity;

    private BigDecimal unitPrice;
    
    private String category;
    

    public OrderItemDocument() {
    }


    public OrderItemDocument(UUID productId, String productName, Integer quantity, BigDecimal unitPrice, String category) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.category = category;
    }


    public UUID getProductId() {
        return this.productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
