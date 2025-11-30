package com.fiap.orderService._webApi.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fiap.orderService._webApi.data.persistence.entities.OrderDocument;
import com.fiap.orderService._webApi.data.persistence.entities.OrderItemDocument;
import com.fiap.orderService._webApi.data.persistence.entities.OrderStatusDocument;
import com.fiap.orderService._webApi.dto.CreateOrderDTO;
import com.fiap.orderService._webApi.dto.CreateOrderItemDTO;
import com.fiap.orderService.core.application.dto.CreateOrderInputDTO;
import com.fiap.orderService.core.application.dto.OrderDTO;
import com.fiap.orderService.core.application.dto.OrderItemDTO;
import com.fiap.orderService.core.application.dto.OrderStatusHistoryDTO;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;

class OrderMapperTest {

    private static final UUID ID = UUID.randomUUID();
    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String CUSTOMER_EMAIL = "test@test.com";
    private static final LocalDateTime DATE = LocalDateTime.now();

    @Test
    void shouldMapCreateOrderDtoToInputDto() {
        CreateOrderItemDTO itemDto = new CreateOrderItemDTO(UUID.randomUUID(), 2);
        CreateOrderDTO requestDto = new CreateOrderDTO(Collections.singletonList(itemDto), CUSTOMER_ID, CUSTOMER_EMAIL);

        CreateOrderInputDTO result = OrderMapper.toCreateOrderInputDTO(requestDto);

        assertNotNull(result);
        assertEquals(requestDto.customerId(), result.customerId());
        assertEquals(requestDto.customerEmail(), result.customerEmail());
        assertEquals(1, result.items().size());
        assertEquals(itemDto.productId(), result.items().get(0).productId());
    }

    @Test
    void shouldMapOrderDtoToDomain() {
        OrderDTO dto = createOrderDTO();

        Order result = OrderMapper.toDomain(dto);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(dto.customerId(), result.getCustomerId());
        assertEquals(1, result.getItems().size());
        assertEquals(1, result.getStatusHistory().size());
    }

    @Test
    void shouldMapDomainToDto() {
        OrderDTO inputDto = createOrderDTO();
        Order order = OrderMapper.toDomain(inputDto); 

        OrderDTO result = OrderMapper.toDTO(order);

        assertNotNull(result);
        assertEquals(order.getId(), result.id());
        assertEquals(order.getCustomerId(), result.customerId());
        assertEquals(order.getPrice(), result.price());
    }

    @Test
    void shouldMapEntityToDto() {
        OrderDocument entity = createOrderDocument();

        OrderDTO result = OrderMapper.toDTO(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getCustomerId(), result.customerId());
        assertEquals(entity.getItems().size(), result.items().size());
    }

    @Test
    void shouldMapDtoToEntity() {
        OrderDTO dto = createOrderDTO();

        OrderDocument result = OrderMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(dto.customerId(), result.getCustomerId());
        assertEquals(dto.statusHistory().get(0).status().toString(), result.getCurrentStatus());
    }

    @Test
    void shouldReturnNullWhenInputsAreNull() {
        assertNull(OrderMapper.toDomain((OrderDTO) null));
        assertNull(OrderMapper.toDTO((Order) null));
        assertNull(OrderMapper.toDTO((OrderDocument) null));
        assertNull(OrderMapper.toEntity(null));
    }

    @Test
    void shouldParseValidJsonItems() {
        String json = "[{\"productId\":\"" + UUID.randomUUID() + "\",\"productName\":\"Test\",\"quantity\":1,\"unitPrice\":10.0,\"category\":\"LANCHE\"}]";
        
        List<OrderItemDTO> items = OrderMapper.parseItemsJson(json);
        
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("Test", items.get(0).productName());
    }

    @Test
    void shouldReturnEmptyListForInvalidJson() {
        List<OrderItemDTO> items = OrderMapper.parseItemsJson("{invalid-json}");
        assertTrue(items.isEmpty());
    }

    private OrderDTO createOrderDTO() {
        OrderItemDTO item = new OrderItemDTO(UUID.randomUUID(), "Burger", 1, BigDecimal.TEN, Category.LANCHE);
        OrderStatusHistoryDTO history = new OrderStatusHistoryDTO(null, OrderStatus.RECEBIDO, DATE);
        
        return OrderDTO.builder()
                .id(ID)
                .customerId(CUSTOMER_ID)
                .customerEmail(CUSTOMER_EMAIL)
                .date(DATE)
                .items(Collections.singletonList(item))
                .statusHistory(Collections.singletonList(history))
                .price(BigDecimal.TEN)
                .build();
    }

    private OrderDocument createOrderDocument() {
        OrderItemDocument itemDoc = new OrderItemDocument();
        itemDoc.setProductId(UUID.randomUUID());
        itemDoc.setProductName("Burger");
        itemDoc.setQuantity(1);
        itemDoc.setUnitPrice(BigDecimal.TEN);
        itemDoc.setCategory("LANCHE");

        OrderStatusDocument historyDoc = new OrderStatusDocument();
        historyDoc.setStatus("RECEBIDO");
        historyDoc.setDate(DATE);

        OrderDocument doc = new OrderDocument();
        doc.setId(ID);
        doc.setCustomerId(CUSTOMER_ID);
        doc.setItems(Collections.singletonList(itemDoc));
        doc.setStatusHistory(Collections.singletonList(historyDoc));
        
        return doc;
    }
}