package com.fiap.orderService.core.gateways.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.orderService.core.application.dto.AttendantDTO;
import com.fiap.orderService.core.domain.entities.Attendant;
import com.fiap.orderService.core.interfaces.AttendantDataSource;

@ExtendWith(MockitoExtension.class)
class AttendantGatewayImplTest {

    @Mock
    private AttendantDataSource dataSource;

    @InjectMocks
    private AttendantGatewayImpl gateway;

    @Test
    void shouldFindAttendantByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        AttendantDTO dto = new AttendantDTO(id, "Test Name", "test@test.com", "123.456.789-00");

        when(dataSource.findById(id)).thenReturn(dto);

        Attendant result = gateway.findById(id);

        assertNotNull(result);
        assertEquals(dto.id(), result.getId());
        assertEquals(dto.name(), result.getName());
        assertEquals(dto.email(), result.getEmail());
        assertEquals(dto.cpf(), result.getFormattedNumber());
        verify(dataSource).findById(id);
    }

    @Test
    void shouldReturnNullWhenAttendantNotFound() {
        UUID id = UUID.randomUUID();

        when(dataSource.findById(id)).thenReturn(null);

        Attendant result = gateway.findById(id);

        assertNull(result);
        verify(dataSource).findById(id);
    }
}