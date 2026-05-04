package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "scoring_results")
public class ScoringResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private ApplicationEntity application;

    @Column(name = "probability", precision = 10, scale = 6)
    private BigDecimal probability;

    @Column(name = "decision", length = 20)
    private String decision;

    @Column(name = "error", columnDefinition = "TEXT")
    private String error;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ScoringResultEntity() {
    }

    public ScoringResultEntity(ApplicationEntity application,
                               BigDecimal probability,
                               String decision,
                               String error,
                               LocalDateTime createdAt) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        this.application = application;
        this.probability = probability;
        this.decision = decision;
        this.error = error;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ApplicationEntity getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntity application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        this.application = application;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}