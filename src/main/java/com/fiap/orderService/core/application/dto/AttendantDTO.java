package com.fiap.orderService.core.application.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AttendantDTO(
        UUID id,
        String name,
        String email,
        String cpf
) {}
