package com.fiap.orderService.core.domain.entities;

import com.fiap.orderService.core.domain.enums.Category;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para a entidade Product.
 */
class ProductTest {

    private static final UUID VALID_ID = UUID.randomUUID();
    private static final String VALID_NAME = "Cheeseburger Clássico";
    private static final BigDecimal VALID_PRICE = new BigDecimal("15.99");
    private static final Category VALID_CATEGORY = Category.LANCHE;

    @Test
    void testProductBuild_SuccessfulCreation() {
        // Act
        Product product = Product.build(VALID_ID, VALID_NAME, VALID_PRICE, VALID_CATEGORY);

        // Assert
        assertNotNull(product);
        assertEquals(VALID_ID, product.getId());
        assertEquals(VALID_NAME, product.getName());
        assertEquals(VALID_PRICE, product.getPrice());
        assertEquals(VALID_CATEGORY, product.getCategory());
    }

    @Test
    void testGetters() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Batata Frita Grande";
        BigDecimal price = new BigDecimal("8.50");
        Category category = Category.ACOMPANHAMENTO;

        // Act
        Product product = Product.build(id, name, price, category);

        // Assert
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
        assertEquals(category, product.getCategory());
    }

    @Test
    void testProductBuild_NullName_ThrowsException() {
        // Arrange
        String name = null;

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Product.build(VALID_ID, name, VALID_PRICE, VALID_CATEGORY);
        });

        assertEquals("O nome do produto deve ser preenchido", thrown.getMessage());
    }

    @Test
    void testProductBuild_EmptyName_ThrowsException() {
        // Arrange
        String name = "   "; // A validação usa trim().isEmpty()

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Product.build(VALID_ID, name, VALID_PRICE, VALID_CATEGORY);
        });

        assertEquals("O nome do produto deve ser preenchido", thrown.getMessage());
    }

    @Test
    void testProductBuild_NullPrice_ThrowsException() {
        // Arrange
        BigDecimal price = null;

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Product.build(VALID_ID, VALID_NAME, price, VALID_CATEGORY);
        });

        assertEquals("O preço do produto deve estar preenchido e não pode ser negativo.", thrown.getMessage());
    }

    @Test
    void testProductBuild_NegativePrice_ThrowsException() {
        // Arrange
        BigDecimal price = new BigDecimal("-0.01");

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Product.build(VALID_ID, VALID_NAME, price, VALID_CATEGORY);
        });

        assertEquals("O preço do produto deve estar preenchido e não pode ser negativo.", thrown.getMessage());
    }

    @Test
    void testProductBuild_ZeroPrice_SuccessfulCreation() {
        // Arrange
        BigDecimal price = BigDecimal.ZERO;

        // Act
        Product product = Product.build(VALID_ID, VALID_NAME, price, VALID_CATEGORY);

        // Assert
        assertNotNull(product);
        assertEquals(BigDecimal.ZERO, product.getPrice());
    }

    @Test
    void testProductBuild_NullCategory_ThrowsException() {
        // Arrange
        Category category = null;

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Product.build(VALID_ID, VALID_NAME, VALID_PRICE, category);
        });

        assertEquals("A categoria do produto deve ser preenchida.", thrown.getMessage());
    }
}