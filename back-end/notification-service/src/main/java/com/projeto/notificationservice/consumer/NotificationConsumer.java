package com.projeto.notificationservice.consumer;

import com.projeto.notificationservice.Dto.ProtocolGeneratedEvent;
import com.projeto.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "notification.email")
    public void handleProtocolGenerated(ProtocolGeneratedEvent event) {
        log.info("📨 Recebida notificação de protocolo: {}", event.getProtocolNumber());

        try {

            emailService.sendComplaintCreatedEmail(
                    event.getUserEmail(),
                    event.getUserName(),
                    Long.valueOf(event.getComplaintId()),
                    "Protocolo: " + event.getProtocolNumber()
            );

            log.info(" Email enviado automaticamente para: {}", event.getUserEmail());

        } catch (Exception e) {
            log.error(" Erro ao enviar email automático", e);
        }
    }
}