package com.fiap.orderService.core.application.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesDTO;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.gateways.order.OrderGateway;

@ExtendWith(MockitoExtension.class)
class ListTodayOrdersUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private ListTodayOrdersUseCase listTodayOrdersUseCase;

    @Test
    void shouldReturnOrdersForSpecificStatuses() {
        // Arrange
        List<String> expectedStatuses = Arrays.asList(
                OrderStatus.RECEBIDO.name(),
                OrderStatus.EM_PREPARACAO.name(),
                OrderStatus.PRONTO.name()
        );
        
        // Simula o retorno do gateway
        OrderWithStatusAndWaitMinutesDTO dto = OrderWithStatusAndWaitMinutesDTO.builder().build();
        List<OrderWithStatusAndWaitMinutesDTO> gatewayResponse = Collections.singletonList(dto);

        // Configura o mock para esperar a lista de status exata e o valor 5
        when(orderGateway.listTodayOrders(expectedStatuses, 5)).thenReturn(gatewayResponse);

        // Act
        List<OrderWithStatusAndWaitMinutesDTO> result = listTodayOrdersUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(gatewayResponse, result);

        // Verifica se o gateway foi chamado com os parâmetros corretos definidos no UseCase
        verify(orderGateway).listTodayOrders(eq(expectedStatuses), eq(5));
    }
}