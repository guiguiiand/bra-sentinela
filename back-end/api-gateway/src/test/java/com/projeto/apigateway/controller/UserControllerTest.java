package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.UserCreateRequest;
import com.projeto.apigateway.controller.dto.UserCreateResponse;
import com.projeto.apigateway.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController controller;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        controller = new UserController(userService);
    }

    @Test
    void createUser_returnsResponse() {
        var req = new UserCreateRequest("name", "email", "99999999999", "12345678900");
        var resp = new UserCreateResponse("1");
        when(userService.createUser(req)).thenReturn(resp);

        var result = controller.getUserById(req);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }
}
