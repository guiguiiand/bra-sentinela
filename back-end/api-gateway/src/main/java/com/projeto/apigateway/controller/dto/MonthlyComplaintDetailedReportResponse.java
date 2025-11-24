package com.projeto.apigateway.controller.dto;

public record MonthlyComplaintDetailedReportResponse(
    String yearMonth,
    Integer year,
    Integer month,
    String monthName,
    Long total,
    Long pending,
    Long open,
    Long inProgress,
    Long validated,
    Long inconsistent,
    Double averagePerDay,
    Double percentageChange
) {}
