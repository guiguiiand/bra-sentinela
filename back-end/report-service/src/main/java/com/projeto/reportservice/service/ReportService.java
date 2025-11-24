package com.projeto.reportservice.service;

import com.projeto.reportservice.config.ComplaintClient;
import com.projeto.reportservice.controller.dto.ComplaintListResponse;
import com.projeto.reportservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final ComplaintClient complaintClient;

    public ReportService(ComplaintClient complaintClient) {
        this.complaintClient = complaintClient;
    }

    public ComplaintReportQuantitiesResponse getReportQuantities() {
        List<ComplaintListResponse> all = Optional.ofNullable(complaintClient.getComplaints())
                .orElse(Collections.emptyList());

        long open = all.stream().filter(c -> safeStatus(c.getStatus()).contains("ABERT")).count();
        long pending = all.stream().filter(c -> {
            String s = safeStatus(c.getStatus());
            return s.contains("PEND") || s.contains("ANALISE") || s.contains("EM ANALISE");
        }).count();
        long validated = all.stream().filter(c -> {
            String s = safeStatus(c.getStatus());
            return s.contains("VALID") || s.contains("VA");
        }).count();
        long inconsistent = all.stream().filter(c -> {
            String s = safeStatus(c.getStatus());
            return s.contains("INCONS") || s.contains("INCONSISTENT");
        }).count();

        return new ComplaintReportQuantitiesResponse(open, pending, validated, inconsistent);
    }

    public List<MonthlyComplaintDetailedReportResponse> getLastMonthsDetailed(int months) {
        List<ComplaintListResponse> all = Optional.ofNullable(complaintClient.getComplaints())
                .orElse(Collections.emptyList());

        // build month range
        java.time.YearMonth now = java.time.YearMonth.now();
        java.util.List<java.time.YearMonth> monthsList = new java.util.ArrayList<>();
        for (int i = months - 1; i >= 0; i--) {
            monthsList.add(now.minusMonths(i));
        }

        java.util.Map<java.time.YearMonth, java.util.List<ComplaintListResponse>> grouped = all.stream()
            .filter(c -> tryParseDate(getBestDateString(c)) != null)
            .collect(java.util.stream.Collectors.groupingBy(c -> java.time.YearMonth.from(tryParseDate(getBestDateString(c)))));

        java.util.List<MonthlyComplaintDetailedReportResponse> result = new java.util.ArrayList<>();
        Long previousTotal = null;
        for (java.time.YearMonth ym : monthsList) {
            java.util.List<ComplaintListResponse> list = grouped.getOrDefault(ym, java.util.Collections.emptyList());
            long total = list.size();
            long pending = list.stream().filter(c -> {
                String s = safeStatus(c.getStatus());
                return s.contains("PEND") || s.contains("ANALISE") || s.contains("EM ANALISE");
            }).count();
            long open = list.stream().filter(c -> safeStatus(c.getStatus()).contains("ABERT")).count();
            long inProgress = list.stream().filter(c -> safeStatus(c.getStatus()).contains("ANALISE") && !safeStatus(c.getStatus()).contains("VALID")).count();
            long validated = list.stream().filter(c -> {
                String s = safeStatus(c.getStatus());
                return s.contains("VALID") || s.contains("VA");
            }).count();
            long inconsistent = list.stream().filter(c -> safeStatus(c.getStatus()).contains("INCONS")).count();

            int daysInMonth = ym.lengthOfMonth();
            double avgPerDay = daysInMonth > 0 ? ((double) total) / daysInMonth : 0.0;
            Double percentageChange = null;
            if (previousTotal != null) {
                if (previousTotal == 0) percentageChange = previousTotal.equals(total) ? 0.0 : 100.0;
                else percentageChange = ((double) (total - previousTotal) / previousTotal) * 100.0;
            }

            MonthlyComplaintDetailedReportResponse dto = new MonthlyComplaintDetailedReportResponse(
                    ym.toString(), ym.getYear(), ym.getMonthValue(), ym.getMonth().name(),
                    total, pending, open, inProgress, validated, inconsistent, avgPerDay, percentageChange
            );
            result.add(dto);
            previousTotal = total;
        }

        return result;
    }

    public byte[] generateComplaintsCsv(String start, String end) {
        try {
            // Define datas padrão se não fornecidas
            if (start == null || start.isBlank()) {
                start = LocalDate.now().minusMonths(5).withDayOfMonth(1).toString();
            }
            if (end == null || end.isBlank()) {
                end = LocalDate.now().toString();
            }

            final LocalDate startDate = parseDateOrDefault(start, LocalDate.now().minusMonths(5).withDayOfMonth(1));
            final LocalDate endDate = parseDateOrDefault(end, LocalDate.now());

            log.info("Generating CSV for range {} -> {}", startDate, endDate);

            // Busca todas as denúncias
            List<ComplaintListResponse> all = Optional.ofNullable(complaintClient.getComplaints())
                    .orElse(Collections.emptyList());

            log.info("ComplaintClient returned {} items", all.size());

            // Filtra por data
            List<ComplaintListResponse> filtered = all.stream()
                    .filter(c -> {
                        LocalDate d = tryParseDate(getBestDateString(c));
                        if (d == null) {
                            log.debug("Skipping complaint with invalid date: id={} protocol={}", 
                                     c.getId(), c.getProtocol());
                            return false;
                        }
                        boolean inRange = !d.isBefore(startDate) && !d.isAfter(endDate);
                        if (!inRange) {
                            log.trace("Complaint out of range: id={} date={}", c.getId(), d);
                        }
                        return inRange;
                    })
                    .collect(Collectors.toList());

            log.info("Filtered complaints count: {}", filtered.size());

            // Gera CSV com BOM para UTF-8 (ajuda Excel a reconhecer encoding)
            StringBuilder sb = new StringBuilder();
            
            // Adiciona BOM UTF-8 (opcional, mas ajuda o Excel)
            sb.append('\uFEFF');
            
            // Header
            sb.append(csvHeader()).append('\n');

            // Rows
            for (ComplaintListResponse c : filtered) {
                sb.append(csvRow(c)).append('\n');
            }

            byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            log.info("CSV generated successfully: {} bytes", bytes.length);
            
            return bytes;
            
        } catch (Exception ex) {
            log.error("Erro ao gerar CSV no backend: {}", ex.getMessage(), ex);
            throw new RuntimeException("Erro ao gerar CSV no backend: " + ex.getMessage(), ex);
        }
    }

    private String csvHeader() {
        return "ID,Protocolo,Data,Canal,Status,Localização,Descrição";
    }

    private String csvRow(ComplaintListResponse c) {
        String id = safeString(c.getId() != null ? c.getId().toString() : "");
        String protocol = safeString(c.getProtocol());
        LocalDate parsed = tryParseDate(getBestDateString(c));
        String date = parsed != null ? parsed.toString() : safeString(getBestDateString(c));
        String channel = safeString(c.getChannel());
        String status = safeString(c.getStatus());
        String location = safeString(c.getLocation()); // <-- Correto agora
        String description = safeString(c.getDescription());

        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                id, protocol, date, channel, status, location, description);
    }

    private String safeString(String input) {
        if (input == null) return "";
        // Escapa aspas duplas e remove quebras de linha
        return input
                .replace("\"", "\"\"")
                .replace('\n', ' ')
                .replace('\r', ' ')
                .trim();
    }

    private LocalDate tryParseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;

        String candidate = dateStr.length() > 10 ? dateStr.substring(0, 10) : dateStr;

        // Tenta ISO formato (yyyy-MM-dd)
        DateTimeFormatter iso = DateTimeFormatter.ISO_LOCAL_DATE;
        try {
            return LocalDate.parse(candidate, iso);
        } catch (DateTimeParseException ex) {
            // Tenta formato brasileiro (dd/MM/yyyy)
            try {
                DateTimeFormatter alt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(candidate, alt);
            } catch (DateTimeParseException ex2) {
                log.warn("Could not parse date: {}", dateStr);
                return null;
            }
        }
    }

    private String getBestDateString(ComplaintListResponse c) {
        if (c == null) return null;
        if (c.getCreatedAt() != null && !c.getCreatedAt().isBlank()) return c.getCreatedAt();
        return c.getDate();
    }

    private LocalDate parseDateOrDefault(String dateStr, LocalDate fallback) {
        LocalDate parsed = tryParseDate(dateStr);
        if (parsed == null) {
            log.warn("Invalid date '{}', falling back to {}", dateStr, fallback);
            return fallback;
        }
        return parsed;
    }

    private String safeStatus(String status) {
        if (status == null) return "";
        return status.toUpperCase().trim();
    }
}