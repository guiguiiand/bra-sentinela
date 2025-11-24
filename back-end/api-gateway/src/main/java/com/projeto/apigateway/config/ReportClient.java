package com.projeto.apigateway.config;

import com.projeto.apigateway.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.apigateway.controller.dto.MonthlyComplaintDetailedReportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "report-service", url = "http://localhost:3005")
public interface ReportClient {

    @GetMapping("/v1/report")
    ComplaintReportQuantitiesResponse getReportQuantities();

    @GetMapping(value = "/v1/report/last-months/pdf", produces = "application/pdf")
    byte[] getLastMonthsPdf(@RequestParam(name = "months", defaultValue = "6") int months);

    @GetMapping("/v1/report/last-months")
    List<MonthlyComplaintDetailedReportResponse> getLastMonthsDetailed(@RequestParam(name = "months", defaultValue = "6") int months);

    @GetMapping(value = "/v1/report/complaints/csv", produces = "text/csv")
    byte[] getComplaintsCsv(@RequestParam(name = "start", required = false) String start, @RequestParam(name = "end", required = false) String end);
}
