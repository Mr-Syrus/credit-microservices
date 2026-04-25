package com.mr_syrus.credit.api.dto;

public class RegistrationEmployeeDto {

    private String username;
    private String mail;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMail() { return mail; }
    public void setMail(String mail) {this.mail = mail; }
    public String getPassword() { return password; }
    public void setPassword(String passwordHash) { this.password = passwordHash; }
}
