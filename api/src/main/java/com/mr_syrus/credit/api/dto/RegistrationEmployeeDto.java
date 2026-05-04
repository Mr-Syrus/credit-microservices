package com.mr_syrus.credit.api.dto;

public class RegistrationEmployeeDto {

    private String username;
    private String mail;
    private String password;

    public RegistrationEmployeeDto() {}

    public RegistrationEmployeeDto(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}