package com.mr_syrus.credit.api.dto;

import java.math.BigDecimal;

public class ScoringEventDto {
    private Integer age;
    private BigDecimal monthlyIncome;
    private BigDecimal creditAmount;
    private Boolean maritalStatus;
    private Integer creditTermMonths;

    public ScoringEventDto() {}

    public ScoringEventDto(Integer age, BigDecimal monthlyIncome, BigDecimal creditAmount,
                           Boolean maritalStatus, Integer creditTermMonths) {
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.creditAmount = creditAmount;
        this.maritalStatus = maritalStatus;
        this.creditTermMonths = creditTermMonths;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Boolean getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Boolean maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getCreditTermMonths() {
        return creditTermMonths;
    }

    public void setCreditTermMonths(Integer creditTermMonths) {
        this.creditTermMonths = creditTermMonths;
    }

}