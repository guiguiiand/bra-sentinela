package com.projeto.notificationservice.Dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintCreatedEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String complaintId;
    private String title;
    private String description;
    private String userEmail;
    private String userName;
    private LocalDateTime createdAt;
}