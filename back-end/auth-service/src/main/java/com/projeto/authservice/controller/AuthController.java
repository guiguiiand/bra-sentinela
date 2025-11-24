package com.projeto.authservice.controller;

import com.projeto.authservice.controller.dto.LoginRequest;
import com.projeto.authservice.controller.dto.LoginResponse;
import com.projeto.authservice.controller.dto.LogoutRequest;
import com.projeto.authservice.controller.dto.RefreshRequest;
import com.projeto.authservice.service.LoginService;
import com.projeto.authservice.service.LogoutService;
import com.projeto.authservice.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;
    private final RefreshTokenService refreshTokenService;
    private final LogoutService logoutService;

    public AuthController(LoginService loginService,
                          RefreshTokenService refreshTokenService,
                          LogoutService logoutService) {
        this.loginService = loginService;
        this.refreshTokenService = refreshTokenService;
        this.logoutService = logoutService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req){

       var resp = loginService.login(req.username(), req.password());

       return ResponseEntity.ok(resp);

    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> login(@RequestBody RefreshRequest req){

        var resp = refreshTokenService.refreshToken(req.refreshToken());

        return ResponseEntity.ok(resp);

    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> login(@AuthenticationPrincipal Jwt jwt,
                                               @RequestBody LogoutRequest req) {

        logoutService.logout(Long.parseLong(jwt.getSubject()), req.refreshToken());

        return ResponseEntity.noContent().build();

    }
    @PostMapping("/logout-all")
    public ResponseEntity<LoginResponse> loginAllDevices(@AuthenticationPrincipal Jwt jwt) {

        logoutService.logoutAll(Long.parseLong(jwt.getSubject()));

        return ResponseEntity.noContent().build();
    }
}
