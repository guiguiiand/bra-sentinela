package com.projeto.apigateway.controller.dto;

import java.util.List;

public record ReportResponde(int complaintsByPeriod, List<String> typesOfScam, List<String> mustUsedChanne) {
}
