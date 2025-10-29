package com.fiap.orderService._webApi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.orderService._webApi.dto.CreateOrderDTO;
import com.fiap.orderService._webApi.mappers.OrderMapper;
import com.fiap.orderService.core.application.dto.OrderResponseDTO;
import com.fiap.orderService.core.controller.OrderController;
import com.fiap.orderService.core.interfaces.OrderDataSource;
import com.fiap.orderService.core.interfaces.ProductDataSource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order")
@Tag(name = "Order", description = "APIs relacionadas aos Pedidos")
public class OrderWebController {

    private final OrderController orderController;

    public OrderWebController(OrderDataSource orderDataSource, ProductDataSource productDataSource) {
        this.orderController = OrderController.build(orderDataSource, productDataSource);
    }

    @Transactional
    @PostMapping("/create")
    @Operation(summary = "Create", description = "Cria um Pedido")
    public ResponseEntity<OrderResponseDTO> save(@RequestBody @Valid CreateOrderDTO order) {
        return ResponseEntity.ok(orderController.create(OrderMapper.toCreateOrderInputDTO(order)));
    }

}
