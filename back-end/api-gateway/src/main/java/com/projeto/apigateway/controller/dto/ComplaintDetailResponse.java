package com.projeto.apigateway.controller.dto;

import com.projeto.apigateway.entity.LocationCity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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