package com.projeto.apigateway.service;

import com.projeto.apigateway.controller.dto.UserCreateRequest;
import com.projeto.apigateway.controller.dto.UserCreateResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserCreateResponse createUser(UserCreateRequest userCreateRequest) {

        String generatedId = java.util.UUID.randomUUID().toString();
        return new UserCreateResponse(generatedId);
    }
}
