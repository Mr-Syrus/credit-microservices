package com.mr_syrus.credit.api.entity;


import com.mr_syrus.credit.api.util.TokenGeneratorUtil;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sessions")
public class SessionEntity {

    @Id
    @Column(name = "session_key", nullable = false, length = 500)
    private String sessionKey;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "ip_address")
    private String ipAddress;

    // User-Agent браузера/клиента
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    protected SessionEntity() {
    }

    public SessionEntity(UserEntity userId,
                         String ipAddress,
                         String userAgent) {
        this.sessionKey = Objects.requireNonNull(
                TokenGeneratorUtil.generateSessionKey(128),
                "Session key cannot be null"
        );
        this.userId = Objects.requireNonNull(userId, "User cannot be null");
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    @PrePersist
    private void prePersist() {
        if (sessionKey == null || sessionKey.isBlank()) {
            throw new IllegalStateException("Session key cannot be null or blank");
        }
        if (userId == null) {
            throw new IllegalStateException("User cannot be null");
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (expiresAt == null) {
            expiresAt = createdAt.plusHours(2);
        }

        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Session has already expired");
        }
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public UserEntity getUser() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getIpAddress() { return ipAddress; }

    public String getUserAgent() { return userAgent; }

}