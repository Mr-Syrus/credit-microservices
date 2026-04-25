package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "work")
public class WorkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_data_id", nullable = false)
    private PersonalDataEntity personalData;

    @Column(name = "organization", nullable = false)
    private String organization;

    @Column(name = "position")
    private String position;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "monthly_income", nullable = false)
    private BigDecimal monthlyIncome;

    @Column(name = "employment_tipe", nullable = false)
    private WorkStatus employmentType;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = Boolean.TRUE;

    public WorkEntity() {
    }

    public WorkEntity(
            PersonalDataEntity personalData,
            String organization,
            String position,
            LocalDateTime startDate,
            LocalDateTime endDate,
            BigDecimal monthlyIncome,
            WorkStatus employmentTipe
    ) {
        this.personalData = requireNonNull(personalData, "PersonalData");
        this.organization = requireNonBlank(organization, "Organization");
        this.position = requireNonBlank(position, "Position");
        this.startDate = requireNonNull(startDate, "Start date");
        this.endDate = endDate; // может быть null, валидация в @PrePersist
        this.monthlyIncome = validateMonthlyIncome(monthlyIncome);
        this.employmentType = requireNonNull(employmentType, "Employment type");
        // isCurrent будет вычислен в @PrePersist
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
        return value;
    }

    private static <T> T requireNonNull(T obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        return obj;
    }

    private static BigDecimal validateMonthlyIncome(BigDecimal income) {
        if (income == null) {
            throw new IllegalArgumentException("Monthly income cannot be null");
        }
        if (income.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly income cannot be negative");
        }
        // Максимальная сумма (опционально, например 10 млн)
        if (income.compareTo(new BigDecimal("10000000")) > 0) {
            throw new IllegalArgumentException("Monthly income cannot exceed 10,000,000");
        }
        return income;
    }

    public Integer getId() {
        return id;
    }

    public PersonalDataEntity getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalDataEntity personalData) {
        this.personalData = requireNonNull(personalData, "PersonalData");
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = requireNonBlank(organization, "Organization");
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = requireNonBlank(position, "Position");
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = requireNonNull(startDate, "Start date");
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = validateMonthlyIncome(monthlyIncome);
    }

    public WorkStatus getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(WorkStatus employmentType) {
        this.employmentType = requireNonNull(employmentType, "Employment type");
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    private void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    @PrePersist
    @PreUpdate
    private void validateAndComputeCurrent() {
        // 1. Автоматическое вычисление isCurrent
        if (endDate == null) {
            this.isCurrent = true;
        } else {
            this.isCurrent = endDate.isAfter(LocalDateTime.now());
        }

        // 2. Бизнес-валидация
        if (startDate == null) {
            throw new IllegalStateException("Start date cannot be null");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalStateException("End date cannot be before start date");
        }

        // 3. Дополнительные правила в зависимости от статуса занятости
        if (employmentType == WorkStatus.STUDENT || employmentType == WorkStatus.UNEMPLOYED) {
            if (monthlyIncome != null && monthlyIncome.compareTo(BigDecimal.ZERO) > 0) {
                throw new IllegalStateException(
                        employmentType + " cannot have positive monthly income"
                );
            }
        }

        if (Boolean.TRUE.equals(this.isCurrent) && this.endDate != null) {
            throw new IllegalStateException("Current employment cannot have an end date");
        }

        if (Boolean.FALSE.equals(this.isCurrent) && this.endDate == null) {
            throw new IllegalStateException("Finished employment must have an end date");
        }

    }
}
