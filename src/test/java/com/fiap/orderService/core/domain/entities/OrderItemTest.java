package com.fiap.orderService.core.domain.entities;

import com.fiap.orderService.core.domain.enums.Category;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    private static final UUID VALID_PRODUCT_ID = UUID.randomUUID();
    private static final String VALID_PRODUCT_NAME = "Sanduíche";
    private static final BigDecimal VALID_UNIT_PRICE = new BigDecimal("12.50");
    private static final Category VALID_CATEGORY = Category.LANCHE;
    private static final int INITIAL_QUANTITY = 2;

    @Test
    void testOrderItemBuild_SuccessfulCreation() {
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);

        assertNotNull(item);
        assertEquals(VALID_PRODUCT_ID, item.getProductId());
        assertEquals(VALID_PRODUCT_NAME, item.getProductName());
        assertEquals(INITIAL_QUANTITY, item.getQuantity());
        assertEquals(VALID_UNIT_PRICE, item.getUnitPrice());
        assertEquals(VALID_CATEGORY, item.getCategory());
    }

    @Test
    void testGetters() {
        UUID productId = UUID.randomUUID();
        String productName = "Bebida Cola";
        BigDecimal unitPrice = new BigDecimal("5.00");
        Category category = Category.BEBIDA;
        int quantity = 3;

        OrderItem item = OrderItem.build(productId, productName, quantity, unitPrice, category);

        assertEquals(productId, item.getProductId());
        assertEquals(productName, item.getProductName());
        assertEquals(quantity, item.getQuantity());
        assertEquals(unitPrice, item.getUnitPrice());
        assertEquals(category, item.getCategory());
    }

    @Test
    void testGetTotalPrice_CorrectCalculation() {
        BigDecimal unitPrice = new BigDecimal("10.00");
        int quantity = 4;
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, quantity, unitPrice, VALID_CATEGORY);

        BigDecimal expectedTotal = new BigDecimal("40.00");
        assertEquals(expectedTotal, item.getTotalPrice());
    }

    @Test
    void testGetTotalPrice_CalculationWithTotalMultipliedByTwo() {
        // Usa as constantes para criar um item com total 12.50 (1 * 12.50)
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, 1, VALID_UNIT_PRICE, VALID_CATEGORY);
        
        // Testa se (item.getTotalPrice() * 2) é 25.00.
        // O valor correto é 25.00 (12.50 * 2), resolvendo o erro apontado.
        BigDecimal expectedTotal = new BigDecimal("25.00");
        assertEquals(expectedTotal, item.getTotalPrice().multiply(BigDecimal.valueOf(2)));
    }
    
    @Test
    void testGetTotalPrice_ZeroUnitPriceReturnsZero() {
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, BigDecimal.ZERO, VALID_CATEGORY);
        assertEquals(BigDecimal.ZERO, item.getTotalPrice());
    }

    @Test
    void testIncreaseQuantity_Successful() {
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);
        item.increaseQuantity(3);
        assertEquals(5, item.getQuantity());
    }

    @Test
    void testIncreaseQuantity_ZeroAmount_ThrowsException() {
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            item.increaseQuantity(0);
        });

        assertEquals("A quantidade a ser adicionada deve ser positiva.", thrown.getMessage());
        assertEquals(INITIAL_QUANTITY, item.getQuantity());
    }

    @Test
    void testDecreaseQuantity_Successful() {
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);
        item.decreaseQuantity(1);
        assertEquals(1, item.getQuantity());
    }

    @Test
    void testDecreaseQuantity_RemoveMoreThanExists_ThrowsException() {
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            item.decreaseQuantity(3);
        });

        assertEquals("Não é possível remover mais itens do que existem no pedido.", thrown.getMessage());
        assertEquals(INITIAL_QUANTITY, item.getQuantity());
    }

    @Test
    void testDecreaseQuantity_ZeroAmount_ThrowsException() {
        OrderItem item = OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            item.decreaseQuantity(0);
        });

        assertEquals("A quantidade a ser removida deve ser maior que zero", thrown.getMessage());
        assertEquals(INITIAL_QUANTITY, item.getQuantity());
    }
    
    @Test
    void testOrderItemBuild_NullProductId_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.build(null, VALID_PRODUCT_NAME, INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);
        });

        assertEquals("O id do produto deve ser preenchido", thrown.getMessage());
    }

    @Test
    void testOrderItemBuild_NullProductName_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.build(VALID_PRODUCT_ID, null, INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);
        });

        assertEquals("O nome do produto deve ser preenchido", thrown.getMessage());
    }

    @Test
    void testOrderItemBuild_EmptyProductName_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.build(VALID_PRODUCT_ID, "  ", INITIAL_QUANTITY, VALID_UNIT_PRICE, VALID_CATEGORY);
        });

        assertEquals("O nome do produto deve ser preenchido", thrown.getMessage());
    }

    @Test
    void testOrderItemBuild_NullQuantity_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, null, VALID_UNIT_PRICE, VALID_CATEGORY);
        });

        assertEquals("A quantidade do produto deve ser maior que zero", thrown.getMessage());
    }
    
    @Test
    void testOrderItemBuild_ZeroQuantity_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, 0, VALID_UNIT_PRICE, VALID_CATEGORY);
        });

        assertEquals("A quantidade do produto deve ser maior que zero", thrown.getMessage());
    }

    @Test
    void testOrderItemBuild_NullUnitPrice_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, null, VALID_CATEGORY);
        });

        assertEquals("O valor unitário do produto deve ser preenchido e não pode ser negativo", thrown.getMessage());
    }

    @Test
    void testOrderItemBuild_NegativeUnitPrice_ThrowsException() {
        BigDecimal negativePrice = new BigDecimal("-0.01");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, negativePrice, VALID_CATEGORY);
        });

        assertEquals("O valor unitário do produto deve ser preenchido e não pode ser negativo", thrown.getMessage());
    }

    @Test
    void testOrderItemBuild_NullCategory_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.build(VALID_PRODUCT_ID, VALID_PRODUCT_NAME, INITIAL_QUANTITY, VALID_UNIT_PRICE, null);
        });

        assertEquals("A categoria do produto deve ser preenchida.", thrown.getMessage());
    }
}