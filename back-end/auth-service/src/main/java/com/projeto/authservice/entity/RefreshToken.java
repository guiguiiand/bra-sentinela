package com.projeto.authservice.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_refresh_tokens")
public class RefreshToken {

    public RefreshToken(UUID familyId, String opaqueHash, User user, Instant issuedAt, Instant expireAt) {
        this.familyId = familyId;
        this.opaqueHash = opaqueHash;
        this.user = user;
        this.issuedAt = issuedAt;
        this.expireAt = expireAt;
    }

    @Id
    @Column(name = "jti", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jti;

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Column(name = "opaque_hash", unique = true, nullable = false)
    private String opaqueHash;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "expire_at", nullable = false)
    private Instant expireAt;

    @Column(name = "revoke_at")
    private Instant revokeAt;

    public RefreshToken() {
    }

    public UUID getJti() {
        return jti;
    }

    public void setJti(UUID jti) {
        this.jti = jti;
    }

    public UUID getFamilyId() {
        return familyId;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }

    public String getOpaqueHash() {
        return opaqueHash;
    }

    public void setOpaqueHash(String opaqueHash) {
        this.opaqueHash = opaqueHash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Instant expireAt) {
        this.expireAt = expireAt;
    }

    public Instant getRevokeAt() {
        return revokeAt;
    }

    public void setRevokeAt(Instant revokeAt) {
        this.revokeAt = revokeAt;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }
}
