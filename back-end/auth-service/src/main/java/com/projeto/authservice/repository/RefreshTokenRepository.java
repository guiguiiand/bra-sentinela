package com.projeto.authservice.repository;

import com.projeto.authservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByOpaqueHash(String opaqueHash);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("update RefreshToken r set r.revokeAt = :revokedAt where r.familyId = :familyId")
    void updateRevokedAtByFamilyId(Instant revokedAt, UUID familyId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("update RefreshToken r set r.revokeAt = :revokedAt where r.opaqueHash = :opaqueHash and r.revokeAt is null")
    int revokeIfNotRevoked(Instant revokedAt, String opaqueHash);

    boolean existsByOpaqueHashAndUserId(String opaqueHash, Long userId);

    @Modifying
    @Query("update RefreshToken r set r.revokeAt = :revokedAt where r.user.id = :userId and r.revokeAt is null")
    void revokedAllFromUserId(Instant revokedAt, Long userId);
}
