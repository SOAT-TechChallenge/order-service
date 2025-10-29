package com.fiap.orderService.core.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.Category;

public class Product {

    private UUID id;

    private String name;

    private BigDecimal price;

    private Category category;

    public Product(UUID id, String name, BigDecimal price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static Product build(UUID id, String name, BigDecimal price, Category category) {
        validate(name, price, category);
        return new Product(id, name, price, category);
    }

    private static void validate(String name, BigDecimal price, Category category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto deve ser preenchido");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço do produto deve estar preenchido e não pode ser negativo.");
        }
        if (category == null) {
            throw new IllegalArgumentException("A categoria do produto deve ser preenchida.");
        }
    }
}
