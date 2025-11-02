package com.fiap.orderService.core.gateways.user;

import java.util.UUID;

import com.fiap.orderService.core.domain.entities.Attendant;

public interface AttendantGateway {

    Attendant findById(UUID id);
}
