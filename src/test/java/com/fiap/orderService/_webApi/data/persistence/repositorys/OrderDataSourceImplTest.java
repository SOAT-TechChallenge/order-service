package com.fiap.orderService._webApi.data.persistence.repositorys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.orderService._webApi.data.persistence.entities.OrderDocument;
import com.fiap.orderService.core.application.dto.OrderDTO;
import com.fiap.orderService.core.application.dto.OrderItemDTO;
import com.fiap.orderService.core.application.dto.OrderStatusHistoryDTO;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesProjection;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class OrderDataSourceImplTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderDataSourceImpl dataSource;

    @Test
    void shouldSaveOrderSuccessfully() {
        // Arrange
        OrderDTO inputDTO = createValidOrderDTO();
        OrderDocument savedEntity = createValidOrderDocument();

        when(repository.save(any(OrderDocument.class))).thenReturn(savedEntity);

        // Act
        OrderDTO result = dataSource.save(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(savedEntity.getId(), result.id());
        verify(repository).save(any(OrderDocument.class));
    }

    @Test
    void shouldFindByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        OrderDocument document = createValidOrderDocument();
        document.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(document));

        OrderDTO result = dataSource.findById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dataSource.findById(id));
    }

    @Test
    void shouldFindByPaymentIdSuccessfully() {
        String paymentId = "PAY-123";
        OrderDocument document = createValidOrderDocument();
        document.setPaymentId(paymentId);

        when(repository.findByPaymentId(paymentId)).thenReturn(Optional.of(document));

        OrderDTO result = dataSource.findByPaymentId(paymentId);

        assertNotNull(result);
        assertEquals(paymentId, result.paymentId());
    }

    @Test
    void shouldThrowExceptionWhenPaymentIdNotFound() {
        String paymentId = "INVALID";
        when(repository.findByPaymentId(paymentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> dataSource.findByPaymentId(paymentId));
    }

    @Test
    void shouldListByClientIdSuccessfully() {
        UUID clientId = UUID.randomUUID();
        List<OrderDocument> docs = Collections.singletonList(createValidOrderDocument());

        when(repository.findAllByCustomerIdOrderByDateDesc(clientId)).thenReturn(docs);

        List<OrderDTO> result = dataSource.listByClientId(clientId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldListTodayOrdersSuccessfully() {
        List<String> statusList = Arrays.asList("RECEBIDO", "PRONTO");
        int minutes = 10;
        
        OrderWithStatusAndWaitMinutesProjection projection = mock(OrderWithStatusAndWaitMinutesProjection.class);
        List<OrderWithStatusAndWaitMinutesProjection> projections = Collections.singletonList(projection);

        when(repository.findTodayOrders(statusList, minutes)).thenReturn(projections);

        List<OrderWithStatusAndWaitMinutesProjection> result = dataSource.listTodayOrders(statusList, minutes);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findTodayOrders(statusList, minutes);
    }

    // Helpers para criar objetos com dados mínimos necessários para o Mapper não falhar
    private OrderDTO createValidOrderDTO() {
        return OrderDTO.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .items(Collections.singletonList(new OrderItemDTO(UUID.randomUUID(), "Item", 1, BigDecimal.TEN, Category.LANCHE)))
                .statusHistory(Collections.singletonList(new OrderStatusHistoryDTO(null, OrderStatus.RECEBIDO, LocalDateTime.now())))
                .build();
    }

    private OrderDocument createValidOrderDocument() {
        OrderDocument doc = new OrderDocument();
        doc.setId(UUID.randomUUID());
        doc.setCustomerId(UUID.randomUUID());
        // Inicializa listas para evitar NullPointerException no Mapper
        doc.setItems(Collections.emptyList());
        doc.setStatusHistory(Collections.emptyList()); 
        return doc;
    }
}