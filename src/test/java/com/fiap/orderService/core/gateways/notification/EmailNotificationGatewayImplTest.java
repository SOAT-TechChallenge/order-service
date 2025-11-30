package com.fiap.orderService.core.gateways.notification;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import com.fiap.orderService.core.domain.enums.OrderStatus;

import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class EmailNotificationGatewayImplTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailNotificationGatewayImpl emailGateway;
    private final String fromAddress = "noreply@test.com";

    @BeforeEach
    void setUp() {
        emailGateway = new EmailNotificationGatewayImpl(mailSender, fromAddress);
    }

    @Test
    void shouldSendEmailSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        OrderStatus status = OrderStatus.PRONTO;
        String toEmail = "customer@test.com";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailGateway.sendEmail(toEmail, orderId, status);

        // Assert
        // Verifica se o sender foi chamado com a mensagem criada
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void shouldBuildHtmlContentCorrectly() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String status = OrderStatus.FINALIZADO.toString();

        // Act
        String content = emailGateway.buildHtmlContent(orderId, status);

        // Assert
        assertTrue(content.contains(orderId.toString()), "Deve conter o ID do pedido");
        assertTrue(content.contains(status), "Deve conter o status do pedido");
        assertTrue(content.contains("<h2>Olá!</h2>"), "Deve conter a saudação HTML");
    }
}