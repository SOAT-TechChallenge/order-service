package com.fiap.orderService.core.gateways.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.orderService._webApi.mappers.OrderMapper;
import com.fiap.orderService.core.application.dto.OrderDTO;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.interfaces.OrderDataSource;

public class OrderGatewayImpl implements OrderGateway {

    private final OrderDataSource dataSource;

    public OrderGatewayImpl(OrderDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Order save(Order order) {
        OrderDTO dto = OrderMapper.toDTO(order);
        dto = dataSource.save(dto);

        return OrderMapper.toDomain(dto);
    }

    @Override
    public Order findById(UUID id) {
        OrderDTO dto = dataSource.findById(id);

        if (dto == null) {
            return null;
        }

        return OrderMapper.toDomain(dto);
    }

    @Override
    public Order findByPaymentId(String paymentId) {
        OrderDTO dto = dataSource.findByPaymentId(paymentId);

        if (dto == null) {
            return null;
        }

        return OrderMapper.toDomain(dto);
    }

    @Override
    public List<Order> listByClientId(UUID clientId) {
        return OrderMapper.toDomain(dataSource.listByClientId(clientId));
    }

    @Override
    public List<Order> listByPeriod(LocalDateTime initialDt, LocalDateTime finalDt) {
        return OrderMapper.toDomain(dataSource.listByPeriod(initialDt, finalDt));

    }

    @Override
    public List<Order> listTodayOrders(List<String> statusList, LocalDateTime initialDt, LocalDateTime finalDt) {
        return OrderMapper.toDomain(dataSource.listTodayOrders(statusList, initialDt, finalDt));
    }

}
