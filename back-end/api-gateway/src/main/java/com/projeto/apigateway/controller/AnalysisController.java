package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.AnalysisRequest;
import com.projeto.apigateway.controller.dto.AnalysisResponse;
import com.projeto.apigateway.service.AnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/analysis")
@AllArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PutMapping
    public ResponseEntity<AnalysisResponse> analyzeComplaints(@RequestBody AnalysisRequest analysisRequest) {
        AnalysisResponse analysisResponse = analysisService.analyzeComplaints(analysisRequest);
        return ResponseEntity.ok(analysisResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalysisResponse> updateComplaintAnalysis(@PathVariable String id, @RequestBody AnalysisRequest analysisRequest) {
        AnalysisResponse analysisResponse = analysisService.updateComplaintAnalysis(id, analysisRequest);
        return ResponseEntity.ok(analysisResponse);
    }
}
