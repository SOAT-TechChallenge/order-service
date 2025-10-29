package com.fiap.orderService.core.controller;

import com.fiap.orderService.core.application.dto.CreateOrderInputDTO;
import com.fiap.orderService.core.application.dto.OrderResponseDTO;
import com.fiap.orderService.core.application.services.OrderCategoryService;
import com.fiap.orderService.core.application.useCases.CreateOrderUseCase;
import com.fiap.orderService.core.gateways.order.OrderGateway;
import com.fiap.orderService.core.gateways.order.OrderGatewayImpl;
import com.fiap.orderService.core.gateways.product.ProductGateway;
import com.fiap.orderService.core.gateways.product.ProductGatewayImpl;
import com.fiap.orderService.core.interfaces.OrderDataSource;
import com.fiap.orderService.core.interfaces.ProductDataSource;
import com.fiap.orderService.core.presenter.OrderPresenter;

public class OrderController {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;
    private final OrderCategoryService orderCategoryService;

    private OrderController(OrderDataSource orderDataSource, ProductDataSource productDataSource) {
        this.orderGateway = new OrderGatewayImpl(orderDataSource);
        this.productGateway = new ProductGatewayImpl(productDataSource);
        this.orderCategoryService = new OrderCategoryService(this.productGateway);
    }

    public static OrderController build(OrderDataSource orderDataSource, ProductDataSource productDataSource) {
        return new OrderController(orderDataSource, productDataSource);
    }

    public OrderResponseDTO create(CreateOrderInputDTO dto) {
        CreateOrderUseCase createOrderUseCase
                = new CreateOrderUseCase(orderGateway, productGateway, orderCategoryService);
        return OrderPresenter.toDTO(createOrderUseCase.execute(dto));
    }

}
