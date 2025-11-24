package com.projeto.apigateway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

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
    private List<String> files;
}