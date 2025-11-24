package com.projeto.reportservice.controller;

import com.projeto.reportservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import com.projeto.reportservice.service.PdfReportService;
import com.projeto.reportservice.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private PdfReportService pdfReportService;

    @Test
    @DisplayName("GET /v1/report returns KPI JSON")
    void getReportQuantities_returnsOkJson() throws Exception {
        ComplaintReportQuantitiesResponse dto = new ComplaintReportQuantitiesResponse(10L, 5L, 3L, 2L);
        when(reportService.getReportQuantities()).thenReturn(dto);

        mockMvc.perform(get("/v1/report").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.open").value(10))
                .andExpect(jsonPath("$.pendingReview").value(5))
                .andExpect(jsonPath("$.validated").value(3))
                .andExpect(jsonPath("$.inconsistent").value(2));
    }

    @Test
    @DisplayName("GET /v1/report/last-months returns detailed list")
    void getLastMonthsDetailed_returnsList() throws Exception {
        MonthlyComplaintDetailedReportResponse item = new MonthlyComplaintDetailedReportResponse(
                "2025-11", 2025, 11, "Nov", 100L, 10L, 60L, 5L, 20L, 5L, 3.33, 12.5
        );
        when(reportService.getLastMonthsDetailed(anyInt())).thenReturn(List.of(item));

        mockMvc.perform(get("/v1/report/last-months").param("months", "6").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].yearMonth").value("2025-11"))
                .andExpect(jsonPath("$[0].total").value(100));
    }

    @Test
    @DisplayName("GET /v1/report/last-months/pdf returns PDF bytes")
    void getLastMonthsPdf_returnsPdf() throws Exception {
        byte[] pdf = new byte[]{0x25, 0x50, 0x44, 0x46}; // %PDF
        when(pdfReportService.generateMonthlyReportPdf(6)).thenReturn(pdf);

        mockMvc.perform(get("/v1/report/last-months/pdf").param("months", "6"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("attachment")))
                .andExpect(content().bytes(pdf));
    }

    @Test
    @DisplayName("GET /v1/report/complaints/csv returns CSV bytes")
    void downloadComplaintsCsv_returnsCsv() throws Exception {
        byte[] csv = "id,createdDate,channel,status\n1,2025-11-01,WhatsApp,open".getBytes();
        when(reportService.generateComplaintsCsv("2025-11-01", "2025-11-30")).thenReturn(csv);

        mockMvc.perform(get("/v1/report/complaints/csv").param("start", "2025-11-01").param("end", "2025-11-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/csv"))
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("denuncias_2025-11-01_to_2025-11-30.csv")))
                .andExpect(content().bytes(csv));
    }

    @Test
    @DisplayName("GET /v1/report/complaints/csv returns JSON error when service fails")
    void downloadComplaintsCsv_serviceError_returnsJsonMessage() throws Exception {
        when(reportService.generateComplaintsCsv(null, null)).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/v1/report/complaints/csv"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Falha ao gerar CSV no servidor. Tente novamente mais tarde."));
    }
}
