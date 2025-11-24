package com.projeto.authservice.service;

import com.projeto.authservice.config.OpaqueToken;
import com.projeto.authservice.repository.RefreshTokenRepository;
import com.projeto.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class LogoutService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public LogoutService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public void logout(Long userId, String opaqueToken) {

        var opaqueHash = OpaqueToken.generateOpaqueHash(opaqueToken);

        var isRefreshFromUser = refreshTokenRepository.existsByOpaqueHashAndUserId(opaqueHash, userId);

        if (!isRefreshFromUser) {
            throw new RuntimeException("Invalid refresh token");
        }

        // inserir o access token atual em um blocklist por 15min - table / redis / cache

        refreshTokenRepository.revokeIfNotRevoked(Instant.now(), opaqueHash);
    }

    @Transactional
    public void logoutAll(Long userId) {

        userRepository.incrementTokenVersion(userId);
        refreshTokenRepository.revokedAllFromUserId(Instant.now(), userId);
    }
}
