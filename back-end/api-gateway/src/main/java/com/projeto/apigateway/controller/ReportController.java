package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.apigateway.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/report")
public class ReportController {
    private final ReportService reportService;
    private final com.projeto.apigateway.config.ReportClient reportClient;

    public ReportController(ReportService reportService, com.projeto.apigateway.config.ReportClient reportClient) {
        this.reportService = reportService;
        this.reportClient = reportClient;
    }

    @GetMapping
    public ResponseEntity<ComplaintReportQuantitiesResponse> generateReportQuantities() {

        return ResponseEntity.ok(reportService.generatedCountReport());
    }

    @GetMapping("/last-months/pdf")
    public ResponseEntity<byte[]> proxyLastMonthsPdf(@RequestParam(name = "months", defaultValue = "6") int months) {
        byte[] pdfBytes = reportClient.getLastMonthsPdf(months);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-denuncias.pdf");
        headers.setContentLength(pdfBytes != null ? pdfBytes.length : 0);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/last-months")
    public ResponseEntity<java.util.List<com.projeto.apigateway.controller.dto.MonthlyComplaintDetailedReportResponse>> proxyLastMonths(
            @RequestParam(name = "months", defaultValue = "6") int months) {

        var response = reportClient.getLastMonthsDetailed(months);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/complaints/csv")
    public ResponseEntity<byte[]> proxyComplaintsCsv(
            @RequestParam(name = "start", required = false) String start,
            @RequestParam(name = "end", required = false) String end) {

        byte[] csv = reportClient.getComplaintsCsv(start, end);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        String fileName = String.format("denuncias_%s_to_%s.csv", (start != null ? start : "inicio"), (end != null ? end : "fim"));
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(csv != null ? csv.length : 0);

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv);
    }
}
