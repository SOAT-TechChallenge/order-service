package com.fiap.orderService.core.application.services;


import java.util.UUID;

import com.fiap.orderService.core.domain.entities.Attendant;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;
import com.fiap.orderService.core.gateways.notification.EmailNotificationGateway;
import com.fiap.orderService.core.gateways.order.OrderGateway;
import com.fiap.orderService.core.gateways.user.AttendantGateway;


public class OrderStatusUpdaterService {

    private final OrderGateway orderGateway;
    private final AttendantGateway attendantGateway;
    private final EmailNotificationGateway emailGateway;

    public OrderStatusUpdaterService(OrderGateway orderGateway, AttendantGateway attendantGateway, EmailNotificationGateway emailGateway) {
        this.orderGateway = orderGateway;
        this.attendantGateway = attendantGateway;
        this.emailGateway = emailGateway;
    }

    public Order moveStatusToPaid(UUID orderId) {
        Order order = findOrder(orderId);
        order.moveStatusToPaid();

        return orderGateway.save(order);
    }

    public Order moveStatusToReceived(UUID orderId, UUID attendantId) {
        validatettendant(attendantId);
        Order order = findOrder(orderId);
        order.moveStatusToReceived(attendantId);
        Order savedOrder = orderGateway.save(order);

        sendNotification(order.getCustomerEmail(), orderId, order.getCurrentStatus());

        return savedOrder;
    }

    public Order moveStatusToInPreparation(UUID orderId, UUID attendantId) {
        Order order = findOrder(orderId);
        validatettendant(attendantId);
        order.moveStatusToInPreparation(attendantId);
        Order savedOrder = orderGateway.save(order);

        sendNotification(order.getCustomerEmail(), orderId, order.getCurrentStatus());

        return savedOrder;
    }

    public Order moveStatusToReady(UUID orderId, UUID attendantId) {
        Order order = findOrder(orderId);
        validatettendant(attendantId);
        order.moveStatusToReady(attendantId);
        Order savedOrder = orderGateway.save(order);

        sendNotification(order.getCustomerEmail(), orderId, order.getCurrentStatus());

        return savedOrder;
    }

    public Order moveStatusToFinished(UUID orderId, UUID attendantId) {
        Order order = findOrder(orderId);
        validatettendant(attendantId);
        order.moveStatusToFinished(attendantId);
        Order savedOrder = orderGateway.save(order);

        sendNotification(order.getCustomerEmail(), orderId, order.getCurrentStatus());

        return savedOrder;
    }

    public Order moveStatusToCanceled(UUID orderId, UUID attendantId) {
        Order order = findOrder(orderId);
        validatettendant(attendantId);
        order.moveStatusToCanceled(attendantId);
        return orderGateway.save(order);
    }

    private void sendNotification(String customerEmail, UUID orderId, OrderStatus status) {
        try {
            emailGateway.sendEmail(customerEmail, orderId, status);
        } catch (Exception e) {

        }
    }

    private Order findOrder(UUID orderId) {
        Order order = orderGateway.findById(orderId);

        if (order == null) {
            throw new EntityNotFoundException("Order");

        }

        return order;
    }

    private Attendant validatettendant(UUID attendantId) {
        if (attendantId == null) {
            return null;
        }

        Attendant attendant = attendantGateway.findById(attendantId);

        if (attendant == null) {
            throw new EntityNotFoundException("Attendant");
        }
        return attendant;
    }
}
