package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.UserCreateRequest;
import com.projeto.apigateway.controller.dto.UserCreateResponse;
import com.projeto.apigateway.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserCreateResponse> getUserById(@RequestBody UserCreateRequest userCreateRequest) {
        UserCreateResponse userResponse = userService.createUser(userCreateRequest);
        return ResponseEntity.ok(userResponse);
    }
}
