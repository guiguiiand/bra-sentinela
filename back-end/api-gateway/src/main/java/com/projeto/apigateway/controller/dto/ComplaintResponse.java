package com.projeto.apigateway.controller.dto;

import lombok.Builder;

@Builder
public record ComplaintResponse(String statusComplaint,
                                String createdDate,
                                String descriptionComplaint,
                                String protocolNumber,
                                String message) {
}
