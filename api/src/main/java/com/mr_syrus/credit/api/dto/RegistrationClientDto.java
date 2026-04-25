package com.mr_syrus.credit.api.dto;

import java.time.LocalDate;

public class RegistrationClientDto {
    // UserEntity
    private String username;
    private String mail;
    private String password;

    // PersonalDataEntity
    private String passportSeries;
    private String passportNumber;
    private String passportIssuedBy;
    private String departmentCode;
    private LocalDate passportIssueDate;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private LocalDate birthDate;
    private String inn;
    private String snils;
    private String phone;

    // RegistrationEntity
    private LocalDate registrationDate;
    private String postalIndex;
    private String migrationDepartment;
    private String region;
    private String district;
    private String city;
    private String street;
    private String house;
    private Integer flat;
    private String registrationType;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassportSeries() { return passportSeries; }
    public void setPassportSeries(String passportSeries) { this.passportSeries = passportSeries; }
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    public String getPassportIssuedBy() { return passportIssuedBy; }
    public void setPassportIssuedBy(String passportIssuedBy) { this.passportIssuedBy = passportIssuedBy; }
    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
    public LocalDate getPassportIssueDate() { return passportIssueDate; }
    public void setPassportIssueDate(LocalDate passportIssueDate) { this.passportIssueDate = passportIssueDate; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getInn() { return inn; }
    public void setInn(String inn) { this.inn = inn; }
    public String getSnils() { return snils; }
    public void setSnils(String snils) { this.snils = snils; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public String getPostalIndex() { return postalIndex; }
    public void setPostalIndex(String postalIndex) { this.postalIndex = postalIndex; }
    public String getMigrationDepartment() { return migrationDepartment; }
    public void setMigrationDepartment(String migrationDepartment) { this.migrationDepartment = migrationDepartment; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getHouse() { return house; }
    public void setHouse(String house) { this.house = house; }
    public Integer getFlat() { return flat; }
    public void setFlat(Integer flat) { this.flat = flat; }
    public String getRegistrationType() { return registrationType; }
    public void setRegistrationType(String registrationType) { this.registrationType = registrationType; }
}
