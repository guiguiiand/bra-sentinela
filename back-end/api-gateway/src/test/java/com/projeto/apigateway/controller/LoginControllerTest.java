package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.LoginRequest;
import com.projeto.apigateway.controller.dto.LoginResponse;
import com.projeto.apigateway.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginService loginService;
    private LoginController controller;

    @BeforeEach
    void setUp() {
        loginService = mock(LoginService.class);
        controller = new LoginController(loginService);
    }

    @Test
    void login_returnsToken() {
        var req = new LoginRequest("joão","password123");
        var resp = new LoginResponse("token");
        when(loginService.login(req)).thenReturn(resp);

        var result = controller.login(req);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }
}
