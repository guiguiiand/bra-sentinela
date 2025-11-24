package com.projeto.reportservice.controller;

import com.projeto.reportservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import com.projeto.reportservice.service.PdfReportService;
import com.projeto.reportservice.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("v1/report")
@Slf4j
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final PdfReportService pdfReportService;

    @GetMapping
    public ResponseEntity<ComplaintReportQuantitiesResponse> generateComplaintReport() {
        return ResponseEntity.ok(reportService.getReportQuantities());
    }

    @GetMapping("/last-months")
    public ResponseEntity<List<MonthlyComplaintDetailedReportResponse>> generateLastNMonthsDetailedReport(
            @RequestParam(name = "months", defaultValue = "6") int months) {
        return ResponseEntity.ok(reportService.getLastMonthsDetailed(months));
    }

    @GetMapping("/last-months/pdf")
    public ResponseEntity<byte[]> generateLastNMonthsDetailedReportPdf(
            @RequestParam(name = "months", defaultValue = "6") int months) {

        byte[] pdfBytes = pdfReportService.generateMonthlyReportPdf(months);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-denuncias.pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/complaints/csv")
    public ResponseEntity<byte[]> downloadComplaintsCsv(
            @RequestParam(name = "start", required = false) String start,
            @RequestParam(name = "end", required = false) String end) {
        try {
            
            byte[] csvBytes = reportService.generateComplaintsCsv(start, end);
            
            if (csvBytes == null || csvBytes.length == 0) {
                return ResponseEntity.noContent().build();
            }

            HttpHeaders headers = new HttpHeaders();
            
            // IMPORTANTE: Content-Type correto com charset UTF-8
            headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
            
            // Nome do arquivo
            String startLabel = (start != null && !start.isBlank()) ? start : "inicio";
            String endLabel = (end != null && !end.isBlank()) ? end : "hoje";
            String fileName = String.format("denuncias_%s_to_%s.csv", startLabel, endLabel);
            
            // Header para download
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(csvBytes.length);
            
            // Permite CORS se necessário
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");

            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvBytes);
                    
        } catch (Exception ex) {
            
            // Retorna erro em JSON para o frontend tratar
            String errorMsg = "Falha ao gerar CSV no servidor. Tente novamente mais tarde.";
            String jsonError = String.format("{\"message\":\"%s\"}", 
                                            errorMsg.replace("\"", "\\\""));
            byte[] errorBytes = jsonError.getBytes(StandardCharsets.UTF_8);
            
            HttpHeaders errorHeaders = new HttpHeaders();
            errorHeaders.setContentType(MediaType.APPLICATION_JSON);
            errorHeaders.setContentLength(errorBytes.length);
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(errorHeaders)
                    .body(errorBytes);
        }
    }
}
