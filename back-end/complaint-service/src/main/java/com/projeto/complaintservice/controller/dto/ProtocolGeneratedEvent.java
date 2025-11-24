package com.projeto.complaintservice.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProtocolGeneratedEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String complaintId;
    private String protocolNumber;
    private String userEmail;
    private String userName;
    private String generatedAt;}