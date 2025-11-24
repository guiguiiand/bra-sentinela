package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.AnalysisRequest;
import com.projeto.apigateway.controller.dto.AnalysisResponse;
import com.projeto.apigateway.controller.dto.AnalysisRequest;
import com.projeto.apigateway.controller.dto.AnalysisResponse;
import com.projeto.apigateway.service.AnalysisService;
import java.util.List;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalysisControllerTest {

    private AnalysisService analysisService;
    private AnalysisController controller;

    @BeforeEach
    void setUp() throws Exception {
        analysisService = mock(AnalysisService.class);
        Class<?> clazz = Class.forName("com.projeto.apigateway.controller.AnalysisController");
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        Object inst = null;
        for (Constructor<?> ctor : ctors) {
            Class<?>[] params = ctor.getParameterTypes();
            if (params.length == 1 && params[0].isAssignableFrom(AnalysisService.class)) {
                ctor.setAccessible(true);
                inst = ctor.newInstance(analysisService);
                break;
            }
        }
        if (inst == null) {
            try {
                inst = clazz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                Constructor<?> ctor = ctors[0];
                Object[] args = new Object[ctor.getParameterCount()];
                inst = ctor.newInstance((Object) args);
            }
        }
        controller = (AnalysisController) inst;
    }

    @Test
    void analyzeComplaints_returnsResponse() {
        var req = new AnalysisRequest("validada", "interno", "publico", List.of());
        var resp = new AnalysisResponse("ok");
        when(analysisService.analyzeComplaints(req)).thenReturn(resp);

        var result = controller.analyzeComplaints(req);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }
}
