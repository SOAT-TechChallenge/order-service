package com.fiap.orderService.core.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OrderWithStatusAndWaitMinutesProjection {

    UUID getOrderId();

    String getStatus();

    LocalDateTime getStatusDt();

    UUID getCustomerId();

    String getCustomerEmail();

    LocalDateTime getOrderDt();

    Integer getWaitTimeMinutes();
}
