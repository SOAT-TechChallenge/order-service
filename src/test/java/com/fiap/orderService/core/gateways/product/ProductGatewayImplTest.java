package com.fiap.orderService.core.gateways.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.orderService.core.application.dto.ProductDTO;
import com.fiap.orderService.core.domain.entities.Product;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.interfaces.ProductDataSource;

@ExtendWith(MockitoExtension.class)
class ProductGatewayImplTest {

    @Mock
    private ProductDataSource dataSource;

    @InjectMocks
    private ProductGatewayImpl gateway;

    @Test
    void shouldListAvailableCategoriesSuccessfully() {
        // Arrange
        List<Category> datasourceCategories = Arrays.asList(Category.LANCHE, Category.BEBIDA);
        when(dataSource.listAvailableCategorys()).thenReturn(datasourceCategories);

        // Act
        List<Category> result = gateway.listAvailableCategorys();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(Category.LANCHE));
        assertTrue(result.contains(Category.BEBIDA));
        verify(dataSource).listAvailableCategorys();
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoriesAvailable() {
        // Arrange
        when(dataSource.listAvailableCategorys()).thenReturn(Collections.emptyList());

        // Act
        List<Category> result = gateway.listAvailableCategorys();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindByIdSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(id, "Coke", BigDecimal.valueOf(5.0), Category.BEBIDA);
        
        when(dataSource.findById(id)).thenReturn(productDTO);

        // Act
        Product result = gateway.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Coke", result.getName());
        assertEquals(BigDecimal.valueOf(5.0), result.getPrice());
        assertEquals(Category.BEBIDA, result.getCategory());
        verify(dataSource).findById(id);
    }
}