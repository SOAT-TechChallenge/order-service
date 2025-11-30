package com.fiap.orderService._webApi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import com.fiap.orderService._webApi.dto.CreateOrderDTO;
import com.fiap.orderService._webApi.dto.CreateOrderItemDTO;
import com.fiap.orderService._webApi.dto.UpdateOrderStatusDTO;
import com.fiap.orderService.core.application.dto.CreateOrderInputDTO;
import com.fiap.orderService.core.application.dto.CreateOrderItemInputDTO;
import com.fiap.orderService.core.application.dto.OrderResponseDTO;
import com.fiap.orderService.core.application.dto.OrderStatusViewDTO;
import com.fiap.orderService.core.application.dto.UpdateOrderStatusInputDTO;
import com.fiap.orderService.core.controller.OrderController;
import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.interfaces.AttendantDataSource;
import com.fiap.orderService.core.interfaces.OrderDataSource;
import com.fiap.orderService.core.interfaces.ProductDataSource;

@ExtendWith(MockitoExtension.class)
class OrderWebControllerTest {

    @Mock
    private OrderController orderController;

    @Mock
    private OrderDataSource orderDataSource;

    @Mock
    private ProductDataSource productDataSource;

    @Mock
    private AttendantDataSource attendantDataSource;

    @Mock
    private JavaMailSender javaMailSender;

    private OrderWebController webController;

    @BeforeEach
    void setUp() {
        webController = new OrderWebController(
                orderDataSource,
                productDataSource,
                attendantDataSource,
                javaMailSender,
                "test@email.com"
        );

        ReflectionTestUtils.setField(webController, "orderController", orderController);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        CreateOrderDTO requestDto = new CreateOrderDTO(createValidItems(), UUID.randomUUID(), "test@email.com");
        OrderResponseDTO responseDto = OrderResponseDTO.builder().id(UUID.randomUUID()).build();

        when(orderController.create(any(CreateOrderInputDTO.class))).thenReturn(responseDto);

        ResponseEntity<OrderResponseDTO> response = webController.save(requestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(orderController).create(any(CreateOrderInputDTO.class));
    }

    @Test
    void shouldFindByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        OrderResponseDTO responseDto = OrderResponseDTO.builder().id(id).build();

        when(orderController.findById(id)).thenReturn(responseDto);

        ResponseEntity<OrderResponseDTO> response = webController.findById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(orderController).findById(id);
    }

    @Test
    void shouldUpdateStatusSuccessfully() {
        UpdateOrderStatusDTO requestDto = new UpdateOrderStatusDTO(UUID.randomUUID(), OrderStatus.PRONTO, UUID.randomUUID());
        OrderResponseDTO responseDto = OrderResponseDTO.builder().id(requestDto.orderId()).build();

        when(orderController.updateStatus(any(UpdateOrderStatusInputDTO.class))).thenReturn(responseDto);

        ResponseEntity<OrderResponseDTO> response = webController.updateStatus(requestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(orderController).updateStatus(any(UpdateOrderStatusInputDTO.class));
    }

    @Test
    void shouldListTodayOrdersSuccessfully() {
        List<OrderStatusViewDTO> listDto = Collections.singletonList(OrderStatusViewDTO.builder().build());

        when(orderController.listTodayOrders()).thenReturn(listDto);

        ResponseEntity<List<OrderStatusViewDTO>> response = webController.listTodayOrders();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listDto, response.getBody());
        verify(orderController).listTodayOrders();
    }

    private List<CreateOrderItemDTO> createValidItems() {
        return Collections.singletonList(
                new CreateOrderItemDTO(UUID.randomUUID(), 1)
        );
    }
}
