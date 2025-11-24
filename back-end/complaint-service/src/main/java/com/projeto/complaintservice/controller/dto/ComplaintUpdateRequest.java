package com.projeto.complaintservice.controller.dto;

import java.util.List;

public record ComplaintUpdateRequest(String statusComplaint,
                                     String internalComment,
                                     String complaintMessage,
                                     List<String> files) {
}
