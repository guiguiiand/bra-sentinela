package com.projeto.authservice.controller;

import com.projeto.authservice.controller.dto.ProfileResponse;
import com.projeto.authservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileControllerTest {

    private UserService userService;
    private ProfileController controller;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        controller = new ProfileController(userService);
    }

    @Test
    void profile_ReturnsProfileFromService() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("7");
        when(jwt.getClaimAsStringList("scope")).thenReturn(List.of("profile:read"));
        when(jwt.getClaimAsStringList("roles")).thenReturn(List.of("ROLE_USER"));

        var expected = new ProfileResponse(7L, "jdoe", 1, List.of("profile:read"), List.of("ROLE_USER"));

        when(userService.readProfile(jwt)).thenReturn(expected);

        ResponseEntity<ProfileResponse> resp = controller.profile(jwt);

        assertEquals(200, resp.getStatusCodeValue());
        assertSame(expected, resp.getBody());
        verify(userService).readProfile(jwt);
    }
}
