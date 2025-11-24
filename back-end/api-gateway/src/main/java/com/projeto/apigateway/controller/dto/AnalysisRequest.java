package com.projeto.apigateway.controller.dto;

import java.util.List;

public record AnalysisRequest(String classification,
                              String internalComment,
                              String publicComment,
                              List<String> files) {
}

