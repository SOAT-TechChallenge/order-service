package com.fiap.orderService.core.application.useCases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fiap.orderService.core.application.dto.CreateOrderInputDTO;
import com.fiap.orderService.core.application.dto.CreateOrderItemInputDTO;
import com.fiap.orderService.core.application.services.OrderCategoryService;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.entities.OrderStatusHistory;
import com.fiap.orderService.core.domain.entities.Product;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.gateways.order.OrderGateway;
import com.fiap.orderService.core.gateways.product.ProductGateway;

public class CreateOrderUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;
    private final OrderCategoryService orderCategoryService;

    public CreateOrderUseCase(OrderGateway orderGateway, ProductGateway productGateway, OrderCategoryService orderCategoryService) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
        this.orderCategoryService = orderCategoryService;
    }

    public Order execute(CreateOrderInputDTO dto) {
        List<OrderItem> items = processOrderItems(dto.items());

        Order order = Order.build(
                null,
                items,
                createInitialStatusHistory(),
                dto.customerId(),
                LocalDateTime.now(),
                null
        );

        return orderGateway.save(order);
    }

    private List<OrderStatusHistory> createInitialStatusHistory() {
        List<OrderStatusHistory> statusHistory = new ArrayList<>();

        OrderStatusHistory status = OrderStatusHistory.build(
                null,
                OrderStatus.PAGAMENTO_PENDENTE,
                LocalDateTime.now()
        );

        statusHistory.add(status);
        return statusHistory;
    }

    private List<OrderItem> processOrderItems(List<CreateOrderItemInputDTO> itemDTOs) {
        Map<UUID, Integer> quantitiesPerProduct = new LinkedHashMap<>();

        for (CreateOrderItemInputDTO dto : itemDTOs) {
            quantitiesPerProduct.merge(dto.productId(), dto.quantity(), (oldQuantity, newQuantity) -> oldQuantity + newQuantity);
        }

        List<OrderItem> items = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : quantitiesPerProduct.entrySet()) {
            UUID productId = entry.getKey();
            Integer totalQuantity = entry.getValue();

            Product product = productGateway.findById(productId);

            OrderItem newItem = OrderItem.build(
                    product.getId(),
                    product.getName(),
                    totalQuantity,
                    product.getPrice(),
                    product.getCategory()
            );

            orderCategoryService.validateCategoryListOrder(items, newItem);
            items.add(newItem);
        }

        return items;
    }

}
