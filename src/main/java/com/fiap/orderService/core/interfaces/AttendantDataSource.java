package com.fiap.orderService.core.interfaces;


import java.util.UUID;

import com.fiap.orderService.core.application.dto.AttendantDTO;

public interface AttendantDataSource {

    AttendantDTO findById(UUID id);
}