package com.fiap.orderService._webApi.data.persistence.repositorys;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fiap.orderService._webApi.data.persistence.entities.OrderDocument;
import com.fiap.orderService._webApi.mappers.OrderMapper;
import com.fiap.orderService.core.application.dto.OrderDTO;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;
import com.fiap.orderService.core.interfaces.OrderDataSource;

@Component
public class OrderDataSourceImpl implements OrderDataSource {

    private final OrderRepository repository;

    public OrderDataSourceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrderDTO save(OrderDTO order) {
        OrderDocument entity = OrderMapper.toEntity(order);
        entity = repository.save(entity);

        return OrderMapper.toDTO(entity);
    }

    @Override
    public OrderDTO findById(UUID id) {
        return OrderMapper.toDTO(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido")));
    }

    @Override
    public OrderDTO findByPaymentId(String paymentId) {
        return OrderMapper.toDTO(repository.findByPaymentId(paymentId).orElseThrow(() -> new EntityNotFoundException("Pedido")));
    }

    @Override
    public List<OrderDTO> listByClientId(UUID clientId) {
        return OrderMapper.toDTO(repository.findAllByCustomerIdOrderByDateDesc(clientId));
    }

    @Override
    public List<OrderDTO> listByPeriod(LocalDateTime initialDt, LocalDateTime finalDt) {
        return OrderMapper.toDTO(repository.findAllByDateBetween(initialDt, finalDt));
    }

    @Override
    public List<OrderDTO> listTodayOrders(List<String> statusList,
            LocalDateTime initialDt,
            LocalDateTime finalDt) {
        return OrderMapper.toDTO(repository.findTodayOrdersByStatusAndDate(statusList, initialDt, finalDt));
    }
}
