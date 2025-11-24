package com.projeto.complaintservice.controller;

import com.projeto.complaintservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.complaintservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import com.projeto.complaintservice.entity.ComplaintEntity;
import com.projeto.complaintservice.repository.ComplaintRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/report")
public class ReportController {

    private final ComplaintRepository complaintRepository;

    public ReportController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @GetMapping
    public ResponseEntity<ComplaintReportQuantitiesResponse> getReportQuantities() {
        List<ComplaintEntity> all = complaintRepository.findAll();

        long open = all.stream().filter(c -> safeStatus(c).contains("ABERT")).count();
        long pending = all.stream().filter(c -> safeStatus(c).contains("PEND") || safeStatus(c).contains("ANALISE") || safeStatus(c).contains("EM ANALISE")).count();
        long validated = all.stream().filter(c -> safeStatus(c).contains("VALID" )|| safeStatus(c).contains("VA")).count();
        long inconsistent = all.stream().filter(c -> safeStatus(c).contains("INCONS" )|| safeStatus(c).contains("INCONSISTENT")).count();

        ComplaintReportQuantitiesResponse resp = new ComplaintReportQuantitiesResponse(open, pending, validated, inconsistent);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/last-months")
    public ResponseEntity<List<MonthlyComplaintDetailedReportResponse>> getLastMonthsDetailed(@RequestParam(name = "months", defaultValue = "6") int months) {
        // For now return a minimal empty list or a basic structure. Report-service will compute/display details.
        List<MonthlyComplaintDetailedReportResponse> empty = new ArrayList<>();
        return ResponseEntity.ok(empty);
    }

    @GetMapping(value = "/last-months/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getLastMonthsPdf(@RequestParam(name = "months", defaultValue = "6") int months) {
        // Not generating PDF in complaint-service; return empty pdf body for compatibility.
        byte[] emptyPdf = new byte[0];
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(emptyPdf);
    }

    private String safeStatus(ComplaintEntity c) {
        if (c == null || c.getStatusComplaint() == null) return "";
        return c.getStatusComplaint().toUpperCase();
    }
}