package com.fiap.orderService.core.application.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// IMPORTAÇÃO DA CLASSE DE DOMÍNIO ORIGINAL
import com.fiap.orderService.core.domain.entities.OrderItem; 
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.exceptions.WrongCategoryOrderException;
import com.fiap.orderService.core.gateways.product.ProductGateway;

class OrderCategoryServiceTest {

    private ProductGateway productGateway;
    private OrderCategoryService orderCategoryService;

    // A ordem correta das categorias conforme o ENUM e a lógica de negócio
    private final List<Category> ORDERED_CATEGORIES = List.of(
        Category.LANCHE, 
        Category.ACOMPANHAMENTO, 
        Category.BEBIDA, 
        Category.SOBREMESA
    );

    @BeforeEach
    void setUp() {
        productGateway = mock(ProductGateway.class);
        
        // Simula o ProductGateway retornando todas as categorias na ordem correta
        when(productGateway.listAvailableCategorys()).thenReturn(ORDERED_CATEGORIES);

        orderCategoryService = new OrderCategoryService(productGateway);
    }

    /**
     * Método auxiliar para criar um mock da classe OrderItem (DOMÍNIO) 
     * e configurar o retorno do método getCategory().
     * Isso resolve o erro de aplicabilidade.
     */
    private OrderItem createOrderItemMock(Category category) {
        OrderItem mockItem = mock(OrderItem.class);
        // Configura o comportamento que o OrderCategoryService vai chamar
        when(mockItem.getCategory()).thenReturn(category);
        return mockItem;
    }

    // --- Casos de Teste ---

    @Test
    void shouldAllowAddingFirstItemIfCategoryIsAvailable() {
        // Cenário 1: Lista vazia. Adicionar LANCHE (OK)
        List<OrderItem> currentItems = Collections.emptyList();
        OrderItem newItem = createOrderItemMock(Category.LANCHE); 

        assertDoesNotThrow(() -> 
            orderCategoryService.validateCategoryListOrder(currentItems, newItem),
            "Deve permitir o primeiro item de qualquer categoria disponível."
        );
    }

    @Test
    void shouldThrowExceptionIfNewCategoryIsNotInAvailableCategories() {
        // Cenário 2: A categoria não está na lista retornada pelo Gateway.
        
        // Simula o Gateway retornando apenas LANCHE
        when(productGateway.listAvailableCategorys()).thenReturn(
            List.of(Category.LANCHE)
        );

        List<OrderItem> currentItems = Collections.emptyList();
        OrderItem newItem = createOrderItemMock(Category.SOBREMESA); 

        // Deve lançar exceção porque SOBREMESA não está disponível (newIndex = -1)
        assertThrows(WrongCategoryOrderException.class, () -> 
            orderCategoryService.validateCategoryListOrder(currentItems, newItem),
            "Deve lançar exceção se a categoria não estiver disponível no Gateway."
        );
    }

    @Test
    void shouldAllowNewCategoryIfItFollowsTheOrder() {
        // Cenário 3: Ordem correta. LANCHE(0) -> ACOMPANHAMENTO(1). maxUsedIndex=0, newIndex=1.
        
        List<OrderItem> currentItems = List.of(createOrderItemMock(Category.LANCHE));
        OrderItem newItem = createOrderItemMock(Category.ACOMPANHAMENTO); 

        assertDoesNotThrow(() -> 
            orderCategoryService.validateCategoryListOrder(currentItems, newItem),
            "Deve permitir nova categoria se o índice for maior ou igual ao máximo usado."
        );
    }

    @Test
    void shouldAllowAddingSameCategory() {
        // Cenário 4: Adicionar a mesma categoria. LANCHE(0) -> LANCHE(0). maxUsedIndex=0, newIndex=0.
        
        List<OrderItem> currentItems = List.of(createOrderItemMock(Category.LANCHE));
        OrderItem newItem = createOrderItemMock(Category.LANCHE); 

        assertDoesNotThrow(() -> 
            orderCategoryService.validateCategoryListOrder(currentItems, newItem),
            "Deve permitir adicionar a mesma categoria."
        );
    }

    @Test
    void shouldThrowExceptionWhenNewCategoryIsBeforeMaxUsedCategory() {
        // Cenário 5: Ordem incorreta. BEBIDA(2) já existe. Adicionar LANCHE(0).
        // maxUsedIndex = 2. newIndex = 0. (0 < 2)
        
        List<OrderItem> currentItems = List.of(createOrderItemMock(Category.BEBIDA));
        OrderItem newItem = createOrderItemMock(Category.LANCHE); 

        assertThrows(WrongCategoryOrderException.class, () -> 
            orderCategoryService.validateCategoryListOrder(currentItems, newItem),
            "Deve lançar exceção se a nova categoria tiver índice menor que o máximo já usado."
        );
    }
    
    @Test
    void shouldIdentifyMaxUsedCategoryCorrectly() {
        // Cenário 6: Verifica o cálculo do maxUsedIndex. 
        // Itens existentes: LANCHE(0) e SOBREMESA(3). maxUsedIndex deve ser 3.
        List<OrderItem> currentItems = List.of(
            createOrderItemMock(Category.LANCHE), 
            createOrderItemMock(Category.SOBREMESA)
        );
        
        // Tentando adicionar BEBIDA (newIndex = 2).
        OrderItem newItem = createOrderItemMock(Category.BEBIDA); 

        // 2 é menor que 3. Deve falhar.
        assertThrows(WrongCategoryOrderException.class, () -> 
            orderCategoryService.validateCategoryListOrder(currentItems, newItem),
            "Deve falhar se a nova categoria for anterior à categoria de maior índice já usada."
        );
    }
}