package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "scoring")
public class ScoringEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private ApplicationEntity application;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "decision", nullable = false, length = 20)
    private String decision;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskStatus riskLevel;

    @Column(name = "probability_default", nullable = false, precision = 5, scale = 4)
    private BigDecimal probabilityDefault;

    @Column(name = "recomended_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal recomendedAmount;

    @Column(name = "recomended_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal recomendedRate;

    // @Column(name = "factors", nullable = false) тип jsonb
    // Поле factors будет добавлено позже

    @Column(name = "model_version", nullable = false, length = 50)
    private String modelVersion;

    @Column(name = "calculated_at", nullable = false, updatable = false)
    private LocalDateTime calculatedAt;

    public ScoringEntity() {
    }

    public ScoringEntity(ApplicationEntity application,
                         Integer score,
                         String decision,
                         RiskStatus riskLevel,
                         BigDecimal probabilityDefault,
                         BigDecimal recomendedAmount,
                         BigDecimal recomendedRate,
                         String modelVersion) {
        this.application = Objects.requireNonNull(application, "Application cannot be null");

        if (score == null || score < 0) {
            throw new IllegalArgumentException("Score must be non-negative");
        }
        this.score = score;

        if (decision == null || decision.isBlank()) {
            throw new IllegalArgumentException("Decision cannot be empty");
        }
        if (decision.length() > 20) {
            throw new IllegalArgumentException("Decision must be less than 20 characters");
        }
        this.decision = decision;

        this.riskLevel = Objects.requireNonNull(riskLevel, "Risk level cannot be null");

        if (probabilityDefault == null ||
                probabilityDefault.compareTo(BigDecimal.ZERO) < 0 ||
                probabilityDefault.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Probability default must be between 0 and 1");
        }
        this.probabilityDefault = probabilityDefault;

        if (recomendedAmount == null || recomendedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Recommended amount must be positive");
        }
        this.recomendedAmount = recomendedAmount;

        if (recomendedRate == null || recomendedRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Recommended rate must be positive");
        }
        this.recomendedRate = recomendedRate;

        if (modelVersion == null || modelVersion.isBlank()) {
            throw new IllegalArgumentException("Model version cannot be empty");
        }
        if (modelVersion.length() > 50) {
            throw new IllegalArgumentException("Model version must be less than 50 characters");
        }
        this.modelVersion = modelVersion;
    }

    @PrePersist
    private void prePersist() {
        if (application == null) {
            throw new IllegalStateException("Application cannot be null");
        }
        if (score == null || score < 0) {
            throw new IllegalStateException("Score must be non-negative");
        }
        if (decision == null || decision.isBlank()) {
            throw new IllegalStateException("Decision cannot be empty");
        }
        if (riskLevel == null) {
            throw new IllegalStateException("Risk level cannot be null");
        }
        if (probabilityDefault == null ||
                probabilityDefault.compareTo(BigDecimal.ZERO) < 0 ||
                probabilityDefault.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalStateException("Probability default must be between 0 and 1");
        }
        if (recomendedAmount == null || recomendedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Recommended amount must be positive");
        }
        if (recomendedRate == null || recomendedRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Recommended rate must be positive");
        }
        if (modelVersion == null || modelVersion.isBlank()) {
            throw new IllegalStateException("Model version cannot be empty");
        }
        if (calculatedAt == null) {
            calculatedAt = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

    public ApplicationEntity getApplication() {
        return application;
    }

    public Integer getScore() {
        return score;
    }

    public String getDecision() {
        return decision;
    }

    public RiskStatus getRiskLevel() {
        return riskLevel;
    }

    public BigDecimal getProbabilityDefault() {
        return probabilityDefault;
    }

    public BigDecimal getRecomendedAmount() {
        return recomendedAmount;
    }

    public BigDecimal getRecomendedRate() {
        return recomendedRate;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
}