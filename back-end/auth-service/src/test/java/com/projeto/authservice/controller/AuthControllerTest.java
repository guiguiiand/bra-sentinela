package com.projeto.authservice.controller;

import com.projeto.authservice.controller.dto.LoginRequest;
import com.projeto.authservice.controller.dto.LoginResponse;
import com.projeto.authservice.controller.dto.LogoutRequest;
import com.projeto.authservice.controller.dto.RefreshRequest;
import com.projeto.authservice.service.LoginService;
import com.projeto.authservice.service.LogoutService;
import com.projeto.authservice.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private LoginService loginService;
    private RefreshTokenService refreshTokenService;
    private LogoutService logoutService;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        loginService = mock(LoginService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        logoutService = mock(LogoutService.class);
        controller = new AuthController(loginService, refreshTokenService, logoutService);
    }

    @Test
    void login_ReturnsOkWithBody() {
        var req = new LoginRequest("user","pass");
        var expected = new LoginResponse("access-token", 3600, "refresh-token", 7200, List.of("read"));

        when(loginService.login("user", "pass")).thenReturn(expected);

        ResponseEntity<LoginResponse> resp = controller.login(req);

        assertEquals(200, resp.getStatusCodeValue());
        assertSame(expected, resp.getBody());
        verify(loginService).login("user", "pass");
    }

    @Test
    void refresh_ReturnsOkWithBody() {
        var req = new RefreshRequest("refresh-token");
        var expected = new LoginResponse("new-access", 1800, "new-refresh", 3600, List.of("read"));

        when(refreshTokenService.refreshToken("refresh-token")).thenReturn(expected);

        ResponseEntity<LoginResponse> resp = controller.login(req);

        assertEquals(200, resp.getStatusCodeValue());
        assertSame(expected, resp.getBody());
        verify(refreshTokenService).refreshToken("refresh-token");
    }

    @Test
    void logout_CallsServiceAndReturnsNoContent() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("42");
        var req = new LogoutRequest("my-refresh");

        ResponseEntity<LoginResponse> resp = controller.login(jwt, req);

        verify(logoutService).logout(42L, "my-refresh");
        assertEquals(204, resp.getStatusCodeValue());
    }

    @Test
    void logoutAll_CallsServiceAndReturnsNoContent() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("99");

        ResponseEntity<LoginResponse> resp = controller.loginAllDevices(jwt);

        verify(logoutService).logoutAll(99L);
        assertEquals(204, resp.getStatusCodeValue());
    }
}
