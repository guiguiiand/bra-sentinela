package com.projeto.authservice.service;

import com.projeto.authservice.controller.dto.ProfileResponse;
import com.projeto.authservice.repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileResponse readProfile(Jwt jwt) {


        var user = userRepository.getReferenceById(Long.valueOf(jwt.getSubject()));

        return new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getTokenVersion(),
                jwt.getClaimAsStringList("scope"),
                jwt.getClaimAsStringList("roles")

        );
    }
}
