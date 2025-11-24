package com.projeto.apigateway.service;

import com.projeto.apigateway.config.ComplaintClient;
import com.projeto.apigateway.controller.dto.AnalysisRequest;
import com.projeto.apigateway.controller.dto.AnalysisResponse;
import com.projeto.apigateway.controller.dto.ComplaintUpdateRequest;
import com.projeto.apigateway.controller.dto.ComplaintUpdateResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnalysisService {

    private final ComplaintClient complaintClient;

    public AnalysisResponse analyzeComplaints(AnalysisRequest analysisRequest) {
        String analysisResponse = "Análise concluída: As reclamações foram analisadas com sucesso.";
        return new AnalysisResponse(analysisResponse);
    }

    public AnalysisResponse updateComplaintAnalysis(String id, AnalysisRequest analysisRequest) {
        ComplaintUpdateRequest req = new ComplaintUpdateRequest(
                analysisRequest.classification(),
                analysisRequest.internalComment(),
                analysisRequest.publicComment(),
                analysisRequest.files()
        );
        ComplaintUpdateResponse resp = complaintClient.updateComplaint(id, req);
        return new AnalysisResponse(resp == null ? "ok" : resp.message());
    }

}
