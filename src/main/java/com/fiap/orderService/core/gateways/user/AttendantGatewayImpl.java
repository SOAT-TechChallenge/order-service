package com.fiap.orderService.core.gateways.user;

import java.util.UUID;

import com.fiap.orderService.core.domain.entities.Attendant;
import com.fiap.orderService.core.interfaces.AttendantDataSource;

public class AttendantGatewayImpl implements AttendantGateway {

    private final AttendantDataSource dataSource;

    public AttendantGatewayImpl(AttendantDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Attendant findById(UUID id) {
        var attendantDto = dataSource.findById(id);

        if (attendantDto == null) {
            return null;
        }

        return Attendant.build(
                attendantDto.id(),
                attendantDto.name(),
                attendantDto.email(),
                attendantDto.cpf()
        );
    }

}
