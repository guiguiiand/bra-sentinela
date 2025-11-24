package com.projeto.authservice.service;

import com.projeto.authservice.config.JwtConfig;
import com.projeto.authservice.config.OpaqueToken;
import com.projeto.authservice.controller.dto.LoginResponse;
import com.projeto.authservice.entity.RefreshToken;
import com.projeto.authservice.entity.User;
import com.projeto.authservice.repository.RefreshTokenRepository;
import com.projeto.authservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;
    private final AccessTokenService accessTokenService;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtConfig jwtConfig, AccessTokenService accessTokenService, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtConfig = jwtConfig;
        this.accessTokenService = accessTokenService;
        this.userRepository = userRepository;
    }

    @Transactional
    public LoginResponse refreshToken(String opaqueToken) {

        // validar se opaque token existe
        var opaqueHash = OpaqueToken.generateOpaqueHash(opaqueToken);
        var refreshToken = getRefreshToken(opaqueHash);

        // validar se ele esta expirado(jogar excecao)
        if(isExpired(refreshToken)) {
            throw new RuntimeException("Refresh Token Expired");
        }

        // validar se o refresh token ja foi utilizado (revogar todos os tokens da familia)
        if (refreshToken.getRevokeAt() != null) {
            logger.warn("Refresh token already revoked - security issue - revoking token family...");
            revokeTokenFamily(refreshToken.getFamilyId());
            throw new RuntimeException("Refresh Token Already Revoked");
        }

        // refresh está valido
        // marcar ele como revogado
        revokeCurrentRefreshToken(refreshToken);

        var user = userRepository.getReferenceById(refreshToken.getUser().getId());

        // emitir um novo access token
        var accesToken = accessTokenService.generateAccessToken(user, refreshToken.getFamilyId());

        // emitir um novo refresh token
        var newRefreshToken = this.generateRefreshToken(user, refreshToken.getFamilyId());


        // retornar os dados de login response

        return new LoginResponse(
                accesToken.getTokenValue(),
                jwtConfig.getExpiresIn(),
                newRefreshToken,
                jwtConfig.getRefreshExpiresIn(),
                accesToken.getClaimAsStringList("scope")
        );

    }

    private void revokeCurrentRefreshToken(RefreshToken refreshToken) {
        var rowsUpdated = refreshTokenRepository.revokeIfNotRevoked(Instant.now(), refreshToken.getOpaqueHash());
        if (rowsUpdated == 0) {
            logger.warn("RaceCondition: Refresh token already revoked - security issue - revoking token family...");
            revokeTokenFamily(refreshToken.getFamilyId());
            throw new RuntimeException("RaceCondition: Refresh Token Already Revoked");
        }
    }

    private void revokeTokenFamily(UUID familyId) {
        refreshTokenRepository.updateRevokedAtByFamilyId(Instant.now(), familyId);
    }

    private RefreshToken getRefreshToken(String opaqueHash) {
        return refreshTokenRepository.findByOpaqueHash(opaqueHash)
                .orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));
    }

    private static boolean isExpired(RefreshToken refreshToken) {
        return Instant.now().isAfter(refreshToken.getExpireAt());
    }

    @Transactional
    public String generateRefreshToken(User user, UUID familyid) {

        var opaqueToken = OpaqueToken.generate();
        var opaqueHash = OpaqueToken.generateOpaqueHash(opaqueToken);

        refreshTokenRepository.save(
                new RefreshToken(
                        familyid,
                        opaqueHash,
                        user,
                        Instant.now(),
                        Instant.now().plusSeconds(jwtConfig.getRefreshExpiresIn())
                )
        );

        return opaqueToken;
    }


}
