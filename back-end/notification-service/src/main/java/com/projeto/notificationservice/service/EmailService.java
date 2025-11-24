package com.projeto.notificationservice.service;

import com.projeto.notificationservice.model.Notification;
import com.projeto.notificationservice.model.NotificationStatus;
import com.projeto.notificationservice.model.NotificationType;
import com.projeto.notificationservice.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final NotificationRepository notificationRepository;
    
    @Value("${notification.email.from}")
    private String fromEmail;

    public void sendSimpleEmail(String to, String subject, String content) {
        log.info("Sending simple email to: {}", to);
        
        Notification notification = Notification.builder()
                .recipient(to)
                .subject(subject)
                .content(content)
                .type(NotificationType.GENERIC)
                .status(NotificationStatus.PENDING)
                .build();
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            
            log.info("Simple email sent successfully to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending simple email to: {}", to, e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
        } finally {
            notificationRepository.save(notification);
        }
    }

    public void sendHtmlEmail(String to, String subject, String templateName, Context context) {
        log.info("Sending HTML email to: {} with template: {}", to, templateName);
        
        try {
            String htmlContent = templateEngine.process(templateName, context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML
            
            mailSender.send(message);
            
            Notification notification = Notification.builder()
                    .recipient(to)
                    .subject(subject)
                    .content(htmlContent)
                    .type(NotificationType.GENERIC)
                    .status(NotificationStatus.SENT)
                    .sentAt(LocalDateTime.now())
                    .build();
            
            notificationRepository.save(notification);
            
            log.info("HTML email sent successfully to: {}", to);
            
        } catch (MessagingException e) {
            log.error("Error sending HTML email to: {}", to, e);
            
            Notification notification = Notification.builder()
                    .recipient(to)
                    .subject(subject)
                    .type(NotificationType.GENERIC)
                    .status(NotificationStatus.FAILED)
                    .errorMessage(e.getMessage())
                    .build();
            
            notificationRepository.save(notification);
        }
    }

    public void sendComplaintCreatedEmail(String to, String userName, Long complaintId, String complaintTitle) {
        log.info("Sending complaint created email to: {}", to);
        
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("complaintId", complaintId);
        context.setVariable("complaintTitle", complaintTitle);
        
        String subject = "Reclamação Criada - #" + complaintId;
        
        sendHtmlEmail(to, subject, "complaint-created", context);
    }

    public void sendProtocolGeneratedEmail(String to, String userName, String protocolNumber, Long complaintId) {
        log.info("Sending protocol generated email to: {}", to);
        
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("protocolNumber", protocolNumber);
        context.setVariable("complaintId", complaintId);
        
        String subject = "Protocolo Gerado - " + protocolNumber;
        
        sendHtmlEmail(to, subject, "protocol-generated", context);
    }
}