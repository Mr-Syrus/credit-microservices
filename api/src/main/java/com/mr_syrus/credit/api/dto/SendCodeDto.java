package com.mr_syrus.credit.api.dto;

public class SendCodeDto {
    private String passportSeries;
    private String passportNumber;
    private String mail;

    public String getPassportSeries() { return passportSeries; }
    public void setPassportSeries(String passportSeries) { this.passportSeries = passportSeries; }
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
}
