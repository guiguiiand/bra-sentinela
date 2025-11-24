package com.projeto.apigateway.service;

import com.projeto.apigateway.controller.dto.LoginRequest;
import com.projeto.apigateway.controller.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public LoginResponse login(LoginRequest loginRequest){
        try {
            String token = generateTokenMock(loginRequest);
            return new LoginResponse(token);
        } catch (RuntimeException exception) {
            throw new RuntimeException("Login failed: " + exception.getMessage());
        }

    }

    private String generateTokenMock(LoginRequest loginRequest){
        if(loginRequest.password().equals("password123") && loginRequest.user().equals("joão")){
            return encodeTokenMock(loginRequest.user());
        }
        throw new RuntimeException("Invalid credentials");

    }

    private String encodeTokenMock(String user) {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9l" +
                "IiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";
    }
}
