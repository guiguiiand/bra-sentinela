package com.projeto.userservice.controller;

import com.projeto.userservice.dto.UserDTO;
import com.projeto.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void create_returnsCreated() {
        var dto = new UserDTO();
        dto.setFullName("A"); dto.setEmail("a@b.com");
        when(userService.create(dto)).thenReturn(dto);

        var resp = controller.create(dto);
        assertEquals(201, resp.getStatusCodeValue());
    }

    @Test
    void findAll_returnsList() {
        when(userService.findAll()).thenReturn(List.of(new UserDTO()));
        var resp = controller.findAll();
        assertEquals(200, resp.getStatusCodeValue());
    }
}
