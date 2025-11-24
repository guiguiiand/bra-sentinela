package com.projeto.reportservice.controller.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintListResponse {
    private UUID id;
    private String protocol;
    private String date;
    private String channel;
    private String status;
    private String location;    
    private String description;
    private String createdAt;
}
