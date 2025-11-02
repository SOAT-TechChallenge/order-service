package com.fiap.orderService.core.application.useCases;

import java.util.UUID;

import com.fiap.orderService.core.application.dto.UpdateOrderStatusInputDTO;
import com.fiap.orderService.core.application.services.OrderStatusUpdaterService;
import com.fiap.orderService.core.domain.entities.Order;

public class UpdateOrderStatusUseCase {

    private final OrderStatusUpdaterService orderStatusUpdaterService;

    public UpdateOrderStatusUseCase(OrderStatusUpdaterService orderStatusUpdaterService) {
        this.orderStatusUpdaterService = orderStatusUpdaterService;
    }

    public void execute(UUID orderId) {
        orderStatusUpdaterService.moveStatusToPaid(orderId);
    }

    public Order execute(UpdateOrderStatusInputDTO dto) {
        return switch (dto.status()) {
            case RECEBIDO ->
                orderStatusUpdaterService.moveStatusToReceived(dto.orderId(), dto.attendantId());

            case EM_PREPARACAO ->
                orderStatusUpdaterService.moveStatusToInPreparation(dto.orderId(), dto.attendantId());

            case PRONTO ->
                orderStatusUpdaterService.moveStatusToReady(dto.orderId(), dto.attendantId());

            case FINALIZADO ->
                orderStatusUpdaterService.moveStatusToFinished(dto.orderId(), dto.attendantId());

            case CANCELADO ->
                orderStatusUpdaterService.moveStatusToCanceled(dto.orderId(), dto.attendantId());

            default ->
                throw new IllegalArgumentException("Invalid Status Change: " + dto.status());
        };
    }
}
