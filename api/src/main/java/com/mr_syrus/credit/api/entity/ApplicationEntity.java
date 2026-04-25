package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "applications")
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personal_data_id", nullable = false)
    private PersonalDataEntity personalData;

    @OneToOne(mappedBy = "application", cascade = CascadeType.PERSIST)
    private ScoringEntity scoring;

    @ManyToOne
    @JoinColumn(name = "credit_id", nullable = false)
    private CreditEntity credit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.ON_REVIEW;

    @Column(name = "credit_term", nullable = false)
    private Integer creditTerm;

    @Column(name = "credit_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal creditAmount;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "completion_date_time")
    private LocalDateTime completionDateTime;

    public ApplicationEntity() {
    }

    public ApplicationEntity(PersonalDataEntity personalData,
                             CreditEntity credit,
                             Integer creditTerm,
                             BigDecimal creditAmount) {
        this.personalData = Objects.requireNonNull(personalData, "Personal data cannot be null");
        this.credit = Objects.requireNonNull(credit, "Credit cannot be null");

        if (creditTerm == null || creditTerm <= 0) {
            throw new IllegalArgumentException("Credit term must be positive");
        }
        this.creditTerm = creditTerm;

        if (creditAmount == null || creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.creditAmount = creditAmount;
    }

    @PrePersist
    private void prePersist() {
        if (personalData == null) {
            throw new IllegalStateException("Personal data cannot be null");
        }
        if (credit == null) {
            throw new IllegalStateException("Credit cannot be null");
        }
        if (creditTerm == null || creditTerm <= 0) {
            throw new IllegalStateException("Credit term must be positive");
        }
        if (creditAmount == null || creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Credit amount must be positive");
        }
        if (status == null) {
            throw new IllegalStateException("Status cannot be null");
        }
    }

    public Integer getId() {
        return id;
    }

    public PersonalDataEntity getPersonalData() {
        return personalData;
    }

    public ScoringEntity getScoring() {
        return scoring;
    }

    public CreditEntity getCredit() {
        return credit;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public Integer getCreditTerm() {
        return creditTerm;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getCompletionDateTime() {
        return completionDateTime;
    }

    public void setStatus(ApplicationStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (this.status == ApplicationStatus.APPROVED || this.status == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Cannot change status of completed application");
        }
        this.status = newStatus;
        if ((newStatus == ApplicationStatus.APPROVED || newStatus == ApplicationStatus.REJECTED)
                && this.completionDateTime == null) {
            this.completionDateTime = LocalDateTime.now();
        }
    }

    public void setScoring(ScoringEntity scoring) {
        if (scoring == null) {
            throw new IllegalArgumentException("Scoring cannot be null");
        }
        if (scoring.getApplication() != this) {
            throw new IllegalArgumentException("Scoring is not associated with this application");
        }
        this.scoring = scoring;
    }
}