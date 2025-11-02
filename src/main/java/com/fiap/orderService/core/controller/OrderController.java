package com.fiap.orderService.core.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;

import com.fiap.orderService.core.application.dto.CreateOrderInputDTO;
import com.fiap.orderService.core.application.dto.OrderResponseDTO;
import com.fiap.orderService.core.application.dto.OrderStatusViewDTO;
import com.fiap.orderService.core.application.dto.UpdateOrderStatusInputDTO;
import com.fiap.orderService.core.application.services.OrderCategoryService;
import com.fiap.orderService.core.application.services.OrderStatusUpdaterService;
import com.fiap.orderService.core.application.useCases.CreateOrderUseCase;
import com.fiap.orderService.core.application.useCases.FindOrderByIdUseCase;
import com.fiap.orderService.core.application.useCases.ListTodayOrdersUseCase;
import com.fiap.orderService.core.application.useCases.UpdateOrderStatusUseCase;
import com.fiap.orderService.core.gateways.notification.EmailNotificationGateway;
import com.fiap.orderService.core.gateways.notification.EmailNotificationGatewayImpl;
import com.fiap.orderService.core.gateways.order.OrderGateway;
import com.fiap.orderService.core.gateways.order.OrderGatewayImpl;
import com.fiap.orderService.core.gateways.product.ProductGateway;
import com.fiap.orderService.core.gateways.product.ProductGatewayImpl;
import com.fiap.orderService.core.gateways.user.AttendantGateway;
import com.fiap.orderService.core.gateways.user.AttendantGatewayImpl;
import com.fiap.orderService.core.interfaces.AttendantDataSource;
import com.fiap.orderService.core.interfaces.OrderDataSource;
import com.fiap.orderService.core.interfaces.ProductDataSource;
import com.fiap.orderService.core.presenter.OrderPresenter;

public class OrderController {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;
    private final OrderCategoryService orderCategoryService;
    private final OrderStatusUpdaterService orderStatusUpdaterService;

    private OrderController(OrderDataSource orderDataSource, ProductDataSource productDataSource, AttendantDataSource attendantDataSource, JavaMailSender javaMailSender, String mailFrom) {
        AttendantGateway attendantGateway = new AttendantGatewayImpl(attendantDataSource);
        EmailNotificationGateway emailGateway = new EmailNotificationGatewayImpl(javaMailSender, mailFrom);
        this.orderGateway = new OrderGatewayImpl(orderDataSource);
        this.productGateway = new ProductGatewayImpl(productDataSource);
        this.orderCategoryService = new OrderCategoryService(this.productGateway);
        this.orderStatusUpdaterService = new OrderStatusUpdaterService(this.orderGateway, attendantGateway, emailGateway);
    }

    public static OrderController build(OrderDataSource orderDataSource, ProductDataSource productDataSource, AttendantDataSource attendantDataSource, JavaMailSender javaMailSender, String mailFrom) {
        return new OrderController(orderDataSource, productDataSource, attendantDataSource, javaMailSender, mailFrom);
    }

    public OrderResponseDTO create(CreateOrderInputDTO dto) {
        CreateOrderUseCase createOrderUseCase
                = new CreateOrderUseCase(orderGateway, productGateway, orderCategoryService);
        return OrderPresenter.toDTO(createOrderUseCase.execute(dto));
    }

    public OrderResponseDTO findById(UUID id) {
        FindOrderByIdUseCase findOrderByIdUseCase = new FindOrderByIdUseCase(orderGateway);
        return OrderPresenter.toDTO(findOrderByIdUseCase.execute(id));
    }

    public OrderResponseDTO updateStatus(UpdateOrderStatusInputDTO dto) {
        UpdateOrderStatusUseCase updateOrderStatusUseCase = new UpdateOrderStatusUseCase(orderStatusUpdaterService);
        return OrderPresenter.toDTO(updateOrderStatusUseCase.execute(dto));
    }

        public List<OrderStatusViewDTO> listTodayOrders() {
        ListTodayOrdersUseCase listTodayOrdersUseCase = new ListTodayOrdersUseCase(orderGateway);
        return OrderPresenter.toStatusViewDTOList(listTodayOrdersUseCase.execute());
    }
}
