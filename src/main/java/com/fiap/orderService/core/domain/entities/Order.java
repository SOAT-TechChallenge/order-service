package com.fiap.orderService.core.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.domain.exceptions.InvalidOrderStatusException;

public class Order {

    private final UUID id;
    private final List<OrderItem> items;
    private final UUID customerId;
    private final String customerEmail;
    private final LocalDateTime date;
    private final List<OrderStatusHistory> statusHistory;
    private String paymentId;

    private Order(UUID id, List<OrderItem> items, UUID customerId, String customerEmail, LocalDateTime date, List<OrderStatusHistory> statusHistory, String paymentId) {
        this.id = id;
        this.items = items;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.date = date;
        this.statusHistory = statusHistory;
        this.paymentId = paymentId;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    public List<OrderStatusHistory> getStatusHistory() {
        return Collections.unmodifiableList(this.statusHistory);
    }

    public BigDecimal getPrice() {
        if (this.items == null) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderStatus getCurrentStatus() {
        if (this.statusHistory == null || this.statusHistory.isEmpty()) {
            throw new IllegalStateException("O pedido não possui histórico de status.");
        }
        
        return this.statusHistory.get(this.statusHistory.size() - 1).getStatus();
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    // public void addItem(UUID productId, String productName, int quantity) {
    //     OrderStatus currentStatus = this.getCurrentStatus();
    //     if (currentStatus == OrderStatus.CANCELADO
    //             || currentStatus == OrderStatus.FINALIZADO) {
    //         throw new InvalidOrderStatusException("Não é possivel adicionar um item ao pedido, pois ele está " + currentStatus);
    //     }
    //     this.items.stream()
    //             .filter(item -> item.getProductId().equals(productId))
    //             .findFirst()
    //             .ifPresentOrElse(
    //                     existingItem -> existingItem.increaseQuantity(quantity),
    //                     () -> this.items.add(OrderItem.build(productId, productName, quantity))
    //             );
    // }
    // public void removeItem(UUID productId, int quantity) {
    //     if (this.getCurrentStatus() != OrderStatus.PAGAMENTO_PENDENTE) {
    //         throw new IllegalStateException("Não é possível remover itens de um pedido que já progrediu no pagamento.");
    //     }
    //     OrderItem itemToRemove = this.items.stream()
    //             .filter(item -> item.getProductId().equals(productId))
    //             .findFirst()
    //             .orElseThrow(() -> new EntityNotFoundException("Product"));
    //     itemToRemove.decreaseQuantity(quantity);
    //     if (itemToRemove.getQuantity() == 0) {
    //         this.items.remove(itemToRemove);
    //         if (this.items.size() <= 1) {
    //             throw new IllegalStateException("O pedido não pode ter menos de um item.");
    //         }
    //     }
    // }
    public void moveStatusToPaid() {
        validateTransition(OrderStatus.PAGAMENTO_PENDENTE);
        addStatus(OrderStatus.PAGO, null);
    }

    public void moveStatusToReceived(UUID attendantId) {
        // validateTransition(OrderStatus.PAGO);
        addStatus(OrderStatus.RECEBIDO, attendantId);
    }

    public void moveStatusToInPreparation(UUID attendantId) {
        validateTransition(OrderStatus.RECEBIDO);
        addStatus(OrderStatus.EM_PREPARACAO, attendantId);
    }

    public void moveStatusToReady(UUID attendantId) {
        validateTransition(OrderStatus.EM_PREPARACAO);
        addStatus(OrderStatus.PRONTO, attendantId);
    }

    public void moveStatusToFinished(UUID attendantId) {
        validateTransition(OrderStatus.PRONTO);
        addStatus(OrderStatus.FINALIZADO, attendantId);
    }

    public void moveStatusToCanceled(UUID attendantId) {
        addStatus(OrderStatus.CANCELADO, attendantId);
    }

    private void validateTransition(OrderStatus expectedCurrentStatus) {
        OrderStatus currentStatus = this.getCurrentStatus();
        if (currentStatus == OrderStatus.FINALIZADO || currentStatus == OrderStatus.CANCELADO) {
            throw new InvalidOrderStatusException("Não é possível alterar status de um pedido que já foi finalizado ou cancelado.");

        } else if (currentStatus != expectedCurrentStatus) {
            throw new InvalidOrderStatusException(
                    "Transição de status inválida. Esperado: " + expectedCurrentStatus
                    + ", mas o status atual é: " + this.getCurrentStatus()
            );
        }
    }

    private void addStatus(OrderStatus newStatus, UUID attendantId) {
        boolean alreadyExists = this.statusHistory.stream()
                .anyMatch(history -> history.getStatus() == newStatus);
        if (alreadyExists) {
            throw new InvalidOrderStatusException("O status " + newStatus + " já foi aplicado a este pedido.");
        }
        this.statusHistory.add(OrderStatusHistory.build(attendantId, newStatus, LocalDateTime.now()));
    }

    public static Order build(UUID id, List<OrderItem> items, List<OrderStatusHistory> statusHistory, UUID customerId, String customerEmail, LocalDateTime date, String paymentId) {
        validate(items, statusHistory, customerId, customerEmail, date);
        return new Order(id, new ArrayList<>(items), customerId, customerEmail, date, new ArrayList<>(statusHistory), paymentId);
    }

    private static void validate(List<OrderItem> items, List<OrderStatusHistory> statusHistory, UUID customerId, String customerEmail, LocalDateTime date) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Os itens do pedido precisam ser preenchidos");
        }
        if (statusHistory == null || statusHistory.isEmpty()) {
            throw new IllegalArgumentException("O histórico de status do pedido precisa ser preenchido");
        }
        if (customerId == null) {
            throw new IllegalArgumentException("O cliente do pedido precisa ser preenchido");
        }
        if (customerEmail == null) {
            throw new IllegalArgumentException("O email do cliente do pedido precisa ser preenchido");
        }
        if (date == null) {
            throw new IllegalArgumentException("A data do pedido precisa ser preenchida");
        }
    }
}
