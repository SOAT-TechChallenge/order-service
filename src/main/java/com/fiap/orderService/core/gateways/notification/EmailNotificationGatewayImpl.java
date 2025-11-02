package com.fiap.orderService.core.gateways.notification;

import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.fiap.orderService.core.domain.enums.OrderStatus;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class EmailNotificationGatewayImpl implements EmailNotificationGateway {

    private final JavaMailSender mailSender;

    private final String fromAddress;

    public EmailNotificationGatewayImpl(JavaMailSender mailSender, String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendEmail(String toEmail, UUID orderId, OrderStatus status) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Atualização do Pedido #" + orderId);

            String htmlContent = buildHtmlContent(orderId, status.toString());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    public String buildHtmlContent(UUID orderId, String status) {
        return """
            <html>
              <body style="font-family: Arial, sans-serif; color: #333;">
                <h2>Olá!</h2>
                <p>Seu pedido <strong>#%s</strong> está agora com o status: <strong>%s</strong>.</p>
                <p>Agradecemos pela sua preferência.</p>
                <br/>
                <p>Atenciosamente,<br/>Equipe Challenge</p>
              </body>
            </html>
            """.formatted(orderId, status);
    }
}
