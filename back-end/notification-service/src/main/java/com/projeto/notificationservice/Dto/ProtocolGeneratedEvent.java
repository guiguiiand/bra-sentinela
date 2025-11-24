package com.projeto.notificationservice.Dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolGeneratedEvent {
    private String complaintId;
    private String protocolNumber;
    private String userEmail;
    private String userName;
    private LocalDateTime generatedAt;
}