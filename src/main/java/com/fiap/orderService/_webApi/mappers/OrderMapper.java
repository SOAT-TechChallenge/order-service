package com.fiap.orderService._webApi.mappers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fiap.orderService._webApi.data.persistence.entities.OrderDocument;
import com.fiap.orderService._webApi.data.persistence.entities.OrderItemDocument;
import com.fiap.orderService._webApi.data.persistence.entities.OrderStatusDocument;
import com.fiap.orderService._webApi.dto.CreateOrderDTO;
import com.fiap.orderService._webApi.dto.CreateOrderItemDTO;
import com.fiap.orderService.core.application.dto.CreateOrderInputDTO;
import com.fiap.orderService.core.application.dto.CreateOrderItemInputDTO;
import com.fiap.orderService.core.application.dto.OrderDTO;
import com.fiap.orderService.core.application.dto.OrderItemDTO;
import com.fiap.orderService.core.application.dto.OrderStatusHistoryDTO;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesDTO;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesProjection;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.entities.OrderStatusHistory;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;

public class OrderMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static CreateOrderInputDTO toCreateOrderInputDTO(CreateOrderDTO requestDTO) {
        List<CreateOrderItemInputDTO> items = requestDTO.items().stream().map(OrderMapper::toCreateOrderItemInputDTO).collect(Collectors.toList());

        return CreateOrderInputDTO.builder()
                .customerId(requestDTO.customerId())
                .customerEmail(requestDTO.customerEmail())
                .items(items)
                .build();
    }

    public static CreateOrderItemInputDTO toCreateOrderItemInputDTO(CreateOrderItemDTO requestDTO) {
        return CreateOrderItemInputDTO.builder()
                .quantity(requestDTO.quantity())
                .productId(requestDTO.productId())
                .build();
    }

    public static Order toDomain(OrderDTO dto) {
        if (dto == null) {
            return null;
        }

        return Order.build(
                dto.id(),
                toItemDomainList(dto.items()),
                toStatusHistoryDomainList(dto.statusHistory()),
                dto.customerId(),
                dto.customerEmail(),
                dto.date(),
                dto.paymentId());
    }

    public static OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemDTO> itemDTOs = toItemDTOList(order.getItems());
        List<OrderStatusHistoryDTO> statusDTOs = toStatusHistoryDTOList(order.getStatusHistory());

        return OrderDTO.builder()
                .id(order.getId())
                .items(itemDTOs)
                .statusHistory(statusDTOs)
                .customerId(order.getCustomerId())
                .customerEmail(order.getCustomerEmail())
                .price(order.getPrice())
                .date(order.getDate())
                .paymentId(order.getPaymentId())
                .build();
    }

    public static OrderDTO toDTO(OrderDocument entity) {
        if (entity == null) {
            return null;
        }

        List<OrderItemDTO> items = toItemDTOFromEntityList(entity.getItems());
        List<OrderStatusHistoryDTO> statusHistory = toStatusHistoryDTOFromEntityList(entity.getStatusHistory());

        return OrderDTO.builder()
                .id(entity.getId())
                .items(items)
                .statusHistory(statusHistory)
                .customerId(entity.getCustomerId())
                .customerEmail(entity.getCustomerEmail())
                .price(entity.getPrice())
                .date(entity.getDate())
                .paymentId(entity.getPaymentId())
                .build();
    }

    public static OrderDocument toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }

        List<OrderItemDocument> items = toItemDocumentList(dto.items());
        List<OrderStatusDocument> statusHistory = toStatusDocumentList(dto.statusHistory());

        String currentStatus = null;
        LocalDateTime lastUpdateDt = null;

        if (!statusHistory.isEmpty()) {
            currentStatus = statusHistory.get(statusHistory.size() - 1).getStatus();
            lastUpdateDt = statusHistory.get(statusHistory.size() - 1).getDate();
        }

        OrderDocument entity = new OrderDocument(
                dto.id(),
                dto.customerId(),
                dto.customerEmail(),
                currentStatus,
                lastUpdateDt,
                items,
                statusHistory,
                dto.price(),
                dto.date(),
                dto.paymentId()
        );
        return entity;
    }

    public static List<OrderItemDTO> parseItemsJson(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        try {
            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, OrderItemDTO.class);
            return objectMapper.readValue(json, listType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<Order> toDomain(List<OrderDTO> orders) {
        return orders.stream().map(OrderMapper::toDomain).collect(Collectors.toList());
    }

    public static List<OrderDTO> toDTO(List<OrderDocument> orders) {
        return orders.stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    public static List<OrderWithStatusAndWaitMinutesDTO> toDtoListFromProjection(List<OrderWithStatusAndWaitMinutesProjection> ordersProjectionList) {
        return ordersProjectionList.stream().map(OrderMapper::toDtoFromProjection).collect(Collectors.toList());
    }

    private static OrderWithStatusAndWaitMinutesDTO toDtoFromProjection(OrderWithStatusAndWaitMinutesProjection projection) {
        return OrderWithStatusAndWaitMinutesDTO.builder()
                .orderId(projection.getOrderId())
                .status(projection.getStatus())
                .statusDt(projection.getStatusDt())
                .customerId(projection.getCustomerId())
                .orderDt(projection.getOrderDt())
                .waitTimeMinutes(projection.getWaitTimeMinutes())
                .build();
    }

    private static List<OrderStatusDocument> toStatusDocumentList(List<OrderStatusHistoryDTO> statusHistory) {
        return statusHistory.stream()
                .map(s -> {
                    OrderStatusDocument status = new OrderStatusDocument();
                    status.setAttendantId(s.attendantId());
                    status.setStatus(s.status().toString());
                    status.setDate(s.date());
                    return status;
                }).collect(Collectors.toList());
    }

    private static List<OrderItemDocument> toItemDocumentList(List<OrderItemDTO> items) {
        return items.stream()
                .map(i -> {
                    OrderItemDocument item = new OrderItemDocument();
                    item.setProductId(i.productId());
                    item.setProductName(i.productName());
                    item.setQuantity(i.quantity());
                    item.setUnitPrice(i.unitPrice());
                    item.setCategory(i.category().toString());
                    return item;
                }).collect(Collectors.toList());
    }

    private static OrderItem toItemDomain(OrderItemDTO item) {
        return OrderItem.build(
                item.productId(),
                item.productName(),
                item.quantity(),
                item.unitPrice(),
                item.category()
        );
    }

    private static OrderItemDTO toItemDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .category(item.getCategory())
                .build();
    }

    private static OrderItemDTO toItemDTOFromEntity(OrderItemDocument item) {
        return OrderItemDTO.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .category(Category.valueOf(item.getCategory()))
                .build();
    }

    private static List<OrderItemDTO> toItemDTOList(List<OrderItem> items) {
        return items.stream().map(OrderMapper::toItemDTO).collect(Collectors.toList());
    }

    private static List<OrderItemDTO> toItemDTOFromEntityList(List<OrderItemDocument> items) {
        return items.stream().map(OrderMapper::toItemDTOFromEntity).collect(Collectors.toList());
    }

    private static List<OrderItem> toItemDomainList(List<OrderItemDTO> items) {
        return items.stream().map(OrderMapper::toItemDomain).collect(Collectors.toList());
    }

    private static OrderStatusHistoryDTO toStatusHistoryDTO(OrderStatusHistory statusHistory) {
        return OrderStatusHistoryDTO.builder()
                .attendantId(statusHistory.getAttendantId())
                .status(statusHistory.getStatus())
                .date(statusHistory.getDate())
                .build();
    }

    private static OrderStatusHistory toStatusHistoryDomain(OrderStatusHistoryDTO dto) {
        return OrderStatusHistory.build(
                dto.attendantId(),
                dto.status(),
                dto.date()
        );
    }

    private static OrderStatusHistoryDTO toStatusHistoryDTOFromEntity(OrderStatusDocument statusHistory) {
        return OrderStatusHistoryDTO.builder()
                .attendantId(statusHistory.getAttendantId())
                .status(OrderStatus.valueOf(statusHistory.getStatus()))
                .date(statusHistory.getDate())
                .build();
    }

    private static List<OrderStatusHistoryDTO> toStatusHistoryDTOList(List<OrderStatusHistory> statusHistory) {
        return statusHistory.stream().map(OrderMapper::toStatusHistoryDTO).collect(Collectors.toList());
    }

    private static List<OrderStatusHistoryDTO> toStatusHistoryDTOFromEntityList(List<OrderStatusDocument> statusHistory) {
        return statusHistory.stream().map(OrderMapper::toStatusHistoryDTOFromEntity).collect(Collectors.toList());
    }

    private static List<OrderStatusHistory> toStatusHistoryDomainList(List<OrderStatusHistoryDTO> statusHistory) {
        return statusHistory.stream().map(OrderMapper::toStatusHistoryDomain).collect(Collectors.toList());
    }
}
