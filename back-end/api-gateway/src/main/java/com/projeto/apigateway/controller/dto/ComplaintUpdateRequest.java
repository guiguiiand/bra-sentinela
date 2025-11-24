package com.projeto.apigateway.controller.dto;

import java.util.List;

public record ComplaintUpdateRequest(String statusComplaint,
                                     String internalComment,
                                     String complaintMessage,
                                     List<String> files) {
}
