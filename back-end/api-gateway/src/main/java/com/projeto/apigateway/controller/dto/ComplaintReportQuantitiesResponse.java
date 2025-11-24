package com.projeto.apigateway.controller.dto;

public record ComplaintReportQuantitiesResponse (Long open, Long pendingReview, Long validated, Long inconsistent ) {
}
