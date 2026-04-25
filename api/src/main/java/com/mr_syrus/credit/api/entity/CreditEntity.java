package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "credits")
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "min_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal minAmount;

    @Column(name = "max_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal maxAmount;

    @Column(name = "min_term_months", nullable = false)
    private Integer minTermMonths;

    @Column(name = "max_term_months", nullable = false)
    private Integer maxTermMonths;

    @Column(name = "min_score", nullable = false)
    private Integer minScore;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public CreditEntity() {
    }

    public CreditEntity(
            String name,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Integer minTermMonths,
            Integer maxTermMonths,
            Integer minScore,
            Boolean active) {
        setName(name);
        setMinAmount(minAmount);
        setMaxAmount(maxAmount);
        setMinTermMonths(minTermMonths);
        setMaxTermMonths(maxTermMonths);
        setMinScore(minScore);
        setActive(active);
    }

    @PrePersist
    @PreUpdate
    private void validateBeforePersist() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("Name cannot be empty");
        }
        if (minAmount == null || minAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Min amount must be positive");
        }
        if (maxAmount == null || maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Max amount must be positive");
        }
        if (minAmount.compareTo(maxAmount) > 0) {
            throw new IllegalStateException("Min amount cannot be greater than max amount");
        }
        if (minTermMonths == null || minTermMonths <= 0) {
            throw new IllegalStateException("Min term must be positive");
        }
        if (maxTermMonths == null || maxTermMonths <= 0) {
            throw new IllegalStateException("Max term must be positive");
        }
        if (minTermMonths > maxTermMonths) {
            throw new IllegalStateException("Min term cannot be greater than max term");
        }
        if (minScore == null || minScore < 0) {
            throw new IllegalStateException("Min score cannot be negative");
        }
        if (active == null) {
            throw new IllegalStateException("Active flag cannot be null");
        }
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public Integer getMinTermMonths() {
        return minTermMonths;
    }

    public Integer getMaxTermMonths() {
        return maxTermMonths;
    }

    public Integer getMinScore() {
        return minScore;
    }

    public Boolean getActive() {
        return active;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Credit name cannot be empty");
        }

        if (name.length() > 50) {
            throw new IllegalArgumentException("Credit name must be less than 255 characters");
        }

        if (name.length() < 3) {
            throw new IllegalArgumentException("Credit name must be at least 3 characters");
        }

        if (!name.matches("^[а-яА-Яa-zA-Z0-9\\s-]+$")) {
            throw new IllegalArgumentException("Credit name can only contain letters, numbers, spaces and hyphens");
        }

        this.name = name.trim();
    }

    public void setMinAmount(BigDecimal minAmount) {
        if (minAmount == null) {
            throw new IllegalArgumentException("Min amount cannot be null");
        }
        if (minAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Min amount must be positive");
        }
        if (maxAmount != null && minAmount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("Min amount cannot be greater than max amount");
        }
        this.minAmount = minAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        if (maxAmount == null) {
            throw new IllegalArgumentException("Max amount cannot be null");
        }
        if (maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Max amount must be positive");
        }
        if (minAmount != null && minAmount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("Max amount cannot be less than min amount");
        }
        this.maxAmount = maxAmount;
    }

    public void setMinTermMonths(Integer minTermMonths) {
        if (minTermMonths == null) {
            throw new IllegalArgumentException("Min term cannot be null");
        }
        if (minTermMonths <= 0) {
            throw new IllegalArgumentException("Min term must be positive");
        }
        if (maxTermMonths != null && minTermMonths > maxTermMonths) {
            throw new IllegalArgumentException("Min term cannot be greater than max term");
        }
        this.minTermMonths = minTermMonths;
    }

    public void setMaxTermMonths(Integer maxTermMonths) {
        if (maxTermMonths == null) {
            throw new IllegalArgumentException("Max term cannot be null");
        }
        if (maxTermMonths <= 0) {
            throw new IllegalArgumentException("Max term must be positive");
        }
        if (minTermMonths != null && minTermMonths > maxTermMonths) {
            throw new IllegalArgumentException("Max term cannot be less than min term");
        }
        this.maxTermMonths = maxTermMonths;
    }

    public void setMinScore(Integer minScore) {
        if (minScore == null) {
            throw new IllegalArgumentException("Min score cannot be null");
        }
        if (minScore < 0) {
            throw new IllegalArgumentException("Min score cannot be negative");
        }
        if (minScore > 1000) {
            throw new IllegalArgumentException("Min score cannot exceed 1000");
        }
        this.minScore = minScore;
    }

    public void setActive(Boolean active) {
        if (active == null) {
            throw new IllegalArgumentException("Active flag cannot be null");
        }
        this.active = active;
    }
}