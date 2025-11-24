package com.projeto.apigateway.service;

import com.projeto.apigateway.config.ReportClient;
import com.projeto.apigateway.controller.dto.ComplaintReportQuantitiesResponse;
import org.springframework.stereotype.Service;
 
@Service
public class ReportService {
    private final ReportClient reportClient;

    public ReportService(ReportClient reportClient) {
        this.reportClient = reportClient;
    }

    public ComplaintReportQuantitiesResponse generatedCountReport() {
        return reportClient.getReportQuantities();
    }
}
