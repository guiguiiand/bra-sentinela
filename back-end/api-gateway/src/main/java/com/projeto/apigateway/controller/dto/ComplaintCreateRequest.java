package com.projeto.apigateway.controller.dto;

import com.projeto.apigateway.entity.LocationCity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ComplaintCreateRequest {
    private String description;
    private String date;
    private String time;
    private String channel;
    private String attackerName;
    private String value;
    private LocationCity locationCity;
    private List<String> files;
    private String customerId;
}
