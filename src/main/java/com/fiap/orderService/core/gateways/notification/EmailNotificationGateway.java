package com.fiap.orderService.core.gateways.notification;

import java.util.UUID;

import com.fiap.orderService.core.domain.enums.OrderStatus;

public interface EmailNotificationGateway {

    void sendEmail(String toEmail, UUID orderId, OrderStatus status);
}
