package com.fiap.orderService.core.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.Category;

public class OrderItem {

    private final UUID productId;
    private final String productName;
    private final BigDecimal unitPrice;
    private final Category category;
    private int quantity;

    private OrderItem(UUID productId, String productName, Integer quantity, BigDecimal unitPrice, Category category) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.category = category;
    }

    public BigDecimal getTotalPrice() {
        if (this.unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    public UUID getProductId() {
        return this.productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public Category getCategory() {
        return this.category;
    }

    public void increaseQuantity(int amountToAdd) {
        if (amountToAdd <= 0) {
            throw new IllegalArgumentException("A quantidade a ser adicionada deve ser positiva.");
        }
        this.quantity += amountToAdd;
    }

    public void decreaseQuantity(int amountToRemove) {
        if (amountToRemove <= 0) {
            throw new IllegalArgumentException("A quantidade a ser removida deve ser maior que zero");
        }
        if (amountToRemove > this.quantity) {
            throw new IllegalArgumentException("Não é possível remover mais itens do que existem no pedido.");
        }
        this.quantity -= amountToRemove;
    }

    public static OrderItem build(UUID productId, String productName, Integer quantity, BigDecimal unitPrice, Category category) {
        validate(productId, productName, quantity, unitPrice, category);
        return new OrderItem(productId, productName, quantity, unitPrice, category);
    }

    private static void validate(UUID productId, String productName, Integer quantity, BigDecimal unitPrice, Category category) {
        if (productId == null) {
            throw new IllegalArgumentException("O id do produto deve ser preenchido");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto deve ser preenchido");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("A quantidade do produto deve ser maior que zero");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor unitário do produto deve ser preenchido e não pode ser negativo");
        }
        if (category == null) {
            throw new IllegalArgumentException("A categoria do produto deve ser preenchida.");
        }
    }
}
