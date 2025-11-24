package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.apigateway.controller.dto.MonthlyComplaintDetailedReportResponse;
import com.projeto.apigateway.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportControllerTest {

    private ReportService reportService;
    private com.projeto.apigateway.config.ReportClient reportClient;
    private ReportController controller;

    @BeforeEach
    void setUp() {
        reportService = mock(ReportService.class);
        reportClient = mock(com.projeto.apigateway.config.ReportClient.class);
        controller = new ReportController(reportService, reportClient);
    }

    @Test
    void generateReportQuantities_returnsOk() {
        var resp = new ComplaintReportQuantitiesResponse(1L,2L,3L,4L);
        when(reportService.generatedCountReport()).thenReturn(resp);

        var result = controller.generateReportQuantities();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }

    @Test
    void generateLastMonths_returnsList() {
        var list = List.of(new MonthlyComplaintDetailedReportResponse("2025-11",2025,11,"November",10L,0L,5L,2L,2L,1L,0.5,0.0));
        when(reportClient.getLastMonthsDetailed(6)).thenReturn(list);

        var result = controller.proxyLastMonths(6);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(list, result.getBody());
    }
}
