package com.projeto.complaintservice.controller.dto;

@Deprecated
public record ComplaintReportQuantitiesResponse (Long open, Long pendingReview, Long validated, Long inconsistent ) {
}
// Deixado intencionalmente vazio. Use report-service para funcionalidades de relatórios.