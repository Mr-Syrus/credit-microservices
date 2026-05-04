package com.mr_syrus.credit.api.dto;

import java.math.BigDecimal;

public class ScoringResponseDto {
    private Integer applicationId;
    private BigDecimal probability;
    private String decision;
    private String error;

    public ScoringResponseDto() {
    }

    public ScoringResponseDto(Integer applicationId, BigDecimal probability, String decision, String error) {
        this.applicationId = applicationId;
        this.probability = probability;
        this.decision = decision;
        this.error = error;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
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

}
