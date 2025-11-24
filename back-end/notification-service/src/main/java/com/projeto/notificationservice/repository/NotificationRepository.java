package com.projeto.notificationservice.repository;

import com.projeto.notificationservice.model.Notification;
import com.projeto.notificationservice.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByRecipient(String recipient);
    
    List<Notification> findByStatus(NotificationStatus status);
    
    List<Notification> findByRecipientAndStatus(String recipient, NotificationStatus status);
}