package com.fiap.orderService.core.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class AttendantDTOTest {

    @Test
    void shouldCreateAttendantDTOUsingBuilder() {
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String email = "john.doe@test.com";
        String cpf = "123.456.789-00";

        AttendantDTO dto = AttendantDTO.builder()
                .id(id)
                .name(name)
                .email(email)
                .cpf(cpf)
                .build();

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(name, dto.name());
        assertEquals(email, dto.email());
        assertEquals(cpf, dto.cpf());
    }

    @Test
    void shouldCreateAttendantDTOUsingConstructor() {
        UUID id = UUID.randomUUID();
        String name = "Jane Doe";
        String email = "jane.doe@test.com";
        String cpf = "098.765.432-11";

        AttendantDTO dto = new AttendantDTO(id, name, email, cpf);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(name, dto.name());
        assertEquals(email, dto.email());
        assertEquals(cpf, dto.cpf());
    }
}