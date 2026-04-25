package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "authorisation_codes")
public class AuthorizationCodeEntity {
    @Id
    @GeneratedValue()
    private UUID id;

    @Column(name = "code", nullable = false, length = 6)
    private String code;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end", nullable = false)
    private LocalDateTime dateTimeEnd;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    protected AuthorizationCodeEntity() {
    }

    public AuthorizationCodeEntity(String code, UserEntity user) {
        this.code = validateCode(code);
        this.user = Objects.requireNonNull(user, "User cannot be null");
    }

    private static String validateCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Authorization code cannot be null or blank");
        }
        String trimmed = code.trim();
        if (trimmed.length() != 6) {
            throw new IllegalArgumentException("Authorization code must be exactly 6 characters");
        }
        if (!trimmed.matches("\\d{6}")) {
            throw new IllegalArgumentException("Authorization code must contain only 6 digits");
        }
        return trimmed;
    }

    @PrePersist
    protected void onCreate() {
        dateTimeStart = LocalDateTime.now();
        dateTimeEnd = dateTimeStart.plusMinutes(3);
    }

    public UUID getId() { return id; }

    public String getCode() { return code; }

    public LocalDateTime getDateTimeStart() { return dateTimeStart; }

    public LocalDateTime getDateTimeEnd() { return dateTimeEnd; }

    public UserEntity getUser() { return user; }
}
