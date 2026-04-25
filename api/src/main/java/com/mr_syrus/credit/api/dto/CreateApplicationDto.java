package com.mr_syrus.credit.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateApplicationDto {
    //параметры кредита
    private Integer creditId;
    private Integer creditTerm;
    private BigDecimal creditAmount;
    //персональные данные для верификации
    private String passportSeries;
    private String passportNumber;
    private String inn;
    private String snils;
    private LocalDate birthDate;
    //данные прописки
    private String postalIndex;
    private String region;
    private String district;
    private String city;
    private String street;
    private String house;
    private Integer flat;

    public Integer getCreditId() {return creditId;}
    public Integer getCreditTerm() {return creditTerm;}
    public BigDecimal getCreditAmount() {return creditAmount;}
    public String getPassportSeries() {return passportSeries;}
    public String getPassportNumber() {return passportNumber;}
    public String getInn() {return inn;}
    public String getSnils() {return snils;}
    public LocalDate getBirthDate() {return birthDate;}
    public String getPostalIndex() {return postalIndex;}
    public String getRegion() {return region;}
    public String getDistrict() {return district;}
    public String getCity() {return city;}
    public String getStreet() {return street;}
    public String getHouse() {return house;}
    public Integer getFlat() {return flat;}

    public void setCreditId(Integer creditId) {this.creditId = creditId;}
    public void setCreditTerm(Integer creditTerm) {this.creditTerm = creditTerm;}
    public void setCreditAmount(BigDecimal creditAmount) {this.creditAmount = creditAmount;}
    public void setPassportSeries(String passportSeries) {this.passportSeries = passportSeries;}
    public void setPassportNumber(String passportNumber) {this.passportNumber = passportNumber;}
    public void setInn(String inn) {this.inn = inn;}
    public void setSnils(String snils) {this.snils = snils;}
    public void setBirthDate(LocalDate birthDate) {this.birthDate = birthDate;}
    public void setPostalIndex(String postalIndex) {this.postalIndex = postalIndex;}
    public void setRegion(String region) {this.region = region;}
    public void setDistrict(String district) {this.district = district;}
    public void setCity(String city) {this.city = city;}
    public void setStreet(String street) {this.street = street;}
    public void setHouse(String house) {this.house = house;}
    public void setFlat(Integer flat) {this.flat = flat;}
}
