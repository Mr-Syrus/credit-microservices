package com.mr_syrus.credit.api.dto;

import com.mr_syrus.credit.api.entity.CreditEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ApplicationResponseDto {
    private Integer id;
    private String status;
    private Integer creditTerm;
    private BigDecimal creditAmount;
    private LocalDateTime createdDateTime;
    private LocalDateTime completionDateTime;
    private String creditProductName;

    public ApplicationResponseDto(Integer id, String status, Integer creditTerm,
                                  BigDecimal creditAmount, LocalDateTime createdDateTime,
                                  LocalDateTime completionDateTime, String creditProductName) {
        this.id = id;
        this.status = status;
        this.creditTerm = creditTerm;
        this.creditAmount = creditAmount;
        this.createdDateTime = createdDateTime;
        this.completionDateTime = completionDateTime;
        this.creditProductName = creditProductName;

    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public Integer getCreditTerm() {return creditTerm;}
    public void setCreditTerm(Integer creditTerm) {this.creditTerm = creditTerm;}
    public BigDecimal getCreditAmount() {return creditAmount;}
    public void setCreditAmount(BigDecimal creditAmount) {this.creditAmount = creditAmount;}
    public LocalDateTime getCreatedDateTime() {return createdDateTime;}
    public void setCreatedDateTime(LocalDateTime createdDateTime) {this.createdDateTime = createdDateTime;}
    public LocalDateTime getCompletionDateTime() {return completionDateTime;}
    public void setCompletionDateTime(LocalDateTime completionDateTime) {this.completionDateTime = completionDateTime;}
    public String getCreditProductName() {return creditProductName;}
    public void setCreditProductName(String creditProductName) {this.creditProductName = creditProductName;}


}
