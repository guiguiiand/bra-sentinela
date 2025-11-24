package com.projeto.complaintservice.controller.dto;

import com.projeto.complaintservice.entity.LocationCity;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintDetailResponse {
    private String protocol;
    private String status;
    private String createdAt;
    private String description;
    private String channel;
    private String attackerName;
    private String value;
    private LocationCity locationCity;
    private List<String> files;
}