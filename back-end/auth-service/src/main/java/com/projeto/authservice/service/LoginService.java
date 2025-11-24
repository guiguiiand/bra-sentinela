package com.projeto.authservice.service;

import com.projeto.authservice.config.JwtConfig;
import com.projeto.authservice.controller.dto.LoginResponse;
import com.projeto.authservice.entity.User;
import com.projeto.authservice.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final JwtConfig jwtConfig;

    public LoginService(UserRepository userRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        RefreshTokenService refreshTokenService,
                        AccessTokenService accessTokenService,
                        JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
        this.jwtConfig = jwtConfig;
    }

    @Transactional
    public LoginResponse login(String username, String password) {

        // VALIDAR USUARIO
        var user = validateUser(username, password);
        // GERAR FAMILIA DO TOKEN
        var familyid = UUID.randomUUID();
        // EMITIR REFRESH TOKEN
        var opaqueToken = refreshTokenService.generateRefreshToken(user, familyid);
        // EMITIR ACCESS TOKEN
        var accessToken = accessTokenService.generateAccessToken(user, familyid);
        // RETORNAR DADOS DE LOGIN RESPONSE

        return new LoginResponse(
                accessToken.getTokenValue(),
                jwtConfig.getExpiresIn(),
                opaqueToken,
                jwtConfig.getRefreshExpiresIn(),
                accessToken.getClaimAsStringList("scope")
        );
    }

    private User validateUser(String username, String password) {
        // VALIDAR USUARIO
        var user = userRepository.findByUsername(username)
                .orElseThrow(RuntimeException::new);

        var isPasswordValid = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!isPasswordValid) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return user;
    }
}
