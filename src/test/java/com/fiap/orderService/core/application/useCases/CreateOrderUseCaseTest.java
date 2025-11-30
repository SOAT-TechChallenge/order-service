package com.fiap.orderService.core.application.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiap.orderService.core.application.dto.CreateOrderInputDTO;
import com.fiap.orderService.core.application.dto.CreateOrderItemInputDTO;
import com.fiap.orderService.core.application.services.OrderCategoryService;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.entities.Product;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.gateways.order.OrderGateway;
import com.fiap.orderService.core.gateways.product.ProductGateway;

class CreateOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private OrderCategoryService orderCategoryService;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        
        CreateOrderItemInputDTO itemDTO = CreateOrderItemInputDTO.builder()
                .productId(productId)
                .quantity(2)
                .build();
                
        CreateOrderInputDTO inputDTO = CreateOrderInputDTO.builder()
                .customerId(customerId)
                .customerEmail("test@test.com")
                .items(Collections.singletonList(itemDTO))
                .build();

        Product product = mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getName()).thenReturn("Burger");
        when(product.getPrice()).thenReturn(BigDecimal.TEN);
        when(product.getCategory()).thenReturn(Category.LANCHE);
        
        when(productGateway.findById(productId)).thenReturn(product);
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order createdOrder = createOrderUseCase.execute(inputDTO);

        // Assert
        assertNotNull(createdOrder);
        assertEquals(customerId, createdOrder.getCustomerId());
        assertEquals(OrderStatus.PAGAMENTO_PENDENTE, createdOrder.getCurrentStatus());
        assertEquals(1, createdOrder.getItems().size());
        assertEquals(2, createdOrder.getItems().get(0).getQuantity());
        
        verify(productGateway).findById(productId);
        verify(orderCategoryService).validateCategoryListOrder(anyList(), any());
        verify(orderGateway).save(any(Order.class));
    }

    @Test
    void shouldMergeDuplicateItemsQuantity() {
        // Arrange
        UUID productId = UUID.randomUUID();
        
        CreateOrderItemInputDTO item1 = CreateOrderItemInputDTO.builder()
                .productId(productId)
                .quantity(2)
                .build();
                
        CreateOrderItemInputDTO item2 = CreateOrderItemInputDTO.builder()
                .productId(productId)
                .quantity(3)
                .build();
        
        CreateOrderInputDTO inputDTO = CreateOrderInputDTO.builder()
                .customerId(UUID.randomUUID())
                .customerEmail("test@test.com")
                .items(Arrays.asList(item1, item2))
                .build();

        Product product = mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getName()).thenReturn("Burger");
        when(product.getPrice()).thenReturn(BigDecimal.TEN);
        when(product.getCategory()).thenReturn(Category.LANCHE);

        when(productGateway.findById(productId)).thenReturn(product);
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order createdOrder = createOrderUseCase.execute(inputDTO);

        // Assert
        assertEquals(1, createdOrder.getItems().size(), "Should have merged items into one");
        assertEquals(5, createdOrder.getItems().get(0).getQuantity(), "Quantity should be summed (2 + 3)");
    }
}