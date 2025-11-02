package com.fiap.orderService._webApi.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import com.fiap.orderService._webApi.dto.CreateOrderDTO;
import com.fiap.orderService._webApi.dto.UpdateOrderStatusDTO;
import com.fiap.orderService._webApi.mappers.OrderMapper;
import com.fiap.orderService.core.application.dto.OrderResponseDTO;
import com.fiap.orderService.core.application.dto.OrderStatusViewDTO;
import com.fiap.orderService.core.application.dto.UpdateOrderStatusInputDTO;
import com.fiap.orderService.core.controller.OrderController;
import com.fiap.orderService.core.interfaces.AttendantDataSource;
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

    public OrderWebController(OrderDataSource orderDataSource, ProductDataSource productDataSource, AttendantDataSource attendantDataSource, JavaMailSender javaMailSender, @Value("${app.mail.from}") String mailFrom) {
        this.orderController = OrderController.build(orderDataSource, productDataSource, attendantDataSource, javaMailSender, mailFrom);
    }

    @Transactional
    @PostMapping("/create")
    @Operation(summary = "Create", description = "Cria um Pedido")
    public ResponseEntity<OrderResponseDTO> save(@RequestBody @Valid CreateOrderDTO order) {
        return ResponseEntity.ok(orderController.create(OrderMapper.toCreateOrderInputDTO(order)));
    }

    @GetMapping("/find-by-id/{id}")
    @Operation(summary = "Find By ID",
            description = "Encontra um pedido pelo ID")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderController.findById(id));
    }

    @Transactional
    @PostMapping("/update-status")
    @Operation(summary = "Update Status",
            description = "Atualiza o status de um pedido")
    public ResponseEntity<OrderResponseDTO> updateStatus(@RequestBody @Valid UpdateOrderStatusDTO dto) {
        return ResponseEntity.ok(orderController.updateStatus(
                UpdateOrderStatusInputDTO.builder()
                        .orderId(dto.orderId())
                        .status(dto.status())
                        .attendantId(dto.attendantId())
                        .build()
        ));
    }

    @GetMapping("/list-today-orders")
    @Operation(summary = "List Today Order",
            description = "Lista os pedidos em Andamento")
    public ResponseEntity<List<OrderStatusViewDTO>> listTodayOrders() {
        return ResponseEntity.ok(orderController.listTodayOrders());
    }
}
