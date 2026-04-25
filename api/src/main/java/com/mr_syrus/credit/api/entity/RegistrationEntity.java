package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "registrations")
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personal_data_id", nullable = false)
    private PersonalDataEntity personalData;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "postal_index", nullable = false, length = 6)
    private String postalIndex;

    @Column(name = "migration_department", nullable = false, length = 300)
    private String migrationDepartment;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "district", nullable = false, length = 100)
    private String district;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "house", nullable = false)
    private String house;

    @Column(name = "flat")
    private Integer flat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationStatus registrationStatus;


    public RegistrationEntity() {
    }

    public RegistrationEntity(
            PersonalDataEntity personalData,
            LocalDate date,
            String postalIndex,
            String migrationDepartment,
            String region,
            String district,
            String city,
            String street,
            String house,
            Integer flat,
            RegistrationStatus registrationStatus
    ) {
        this.personalData = Objects.requireNonNull(personalData, "Personal data cannot be null");

        this.date = Objects.requireNonNull(date, "Date cannot be null");

        this.postalIndex = validateAndCleanPostalIndex(postalIndex);

        if (migrationDepartment == null || migrationDepartment.isBlank()) {
            throw new IllegalArgumentException("Migration department cannot be null or blank");
        }
        this.migrationDepartment = migrationDepartment;

        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException("Region cannot be null or blank");
        }
        this.region = region;

        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException("District cannot be null or blank");
        }
        this.district = district;

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or blank");
        }
        this.city = city;

        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be null or blank");
        }
        this.street = street;

        if (house == null || house.isBlank()) {
            throw new IllegalArgumentException("House cannot be null or blank");
        }
        this.house = house;

        if (flat != null && flat <= 0) {
            throw new IllegalArgumentException("Flat number must be positive");
        }
        this.flat = flat;

        this.registrationStatus = Objects.requireNonNull(registrationStatus, "Registration status cannot be null");
    }

    private static String validateAndCleanPostalIndex(String rawPostalIndex) {
        if (rawPostalIndex == null) {
            throw new IllegalArgumentException("Postal index cannot be null");
        }
        String cleaned = rawPostalIndex.replaceAll("[-\\s]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Postal index cannot be empty after removing delimiters");
        }
        if (cleaned.length() != 6) {
            throw new IllegalArgumentException(
                    "Postal index must be exactly 6 digits after removing delimiters, but got: " + cleaned.length()
            );
        }
        if (!cleaned.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "Postal index must contain only digits, but got: " + cleaned
            );
        }
        return cleaned;
    }

    @PrePersist
    private void prePersist() {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (postalIndex == null) {
            throw new IllegalStateException("Postal index cannot be null");
        }
        if (postalIndex.length() != 6) {
            throw new IllegalStateException("Postal index must be exactly 6 digits");
        }
        if (!postalIndex.matches("\\d+")) {
            throw new IllegalStateException("Postal index must contain only digits");
        }

        if (migrationDepartment == null || migrationDepartment.isBlank()) {
            throw new IllegalStateException("Migration department cannot be null or blank");
        }

        if (region == null || region.isBlank()) {
            throw new IllegalStateException("Region cannot be null or blank");
        }

        if (district == null || district.isBlank()) {
            throw new IllegalStateException("District cannot be null or blank");
        }

        if (city == null || city.isBlank()) {
            throw new IllegalStateException("City cannot be null or blank");
        }

        if (street == null || street.isBlank()) {
            throw new IllegalStateException("Street cannot be null or blank");
        }

        if (house == null || house.isBlank()) {
            throw new IllegalStateException("House cannot be null or blank");
        }

        if (flat != null && flat <= 0) {
            throw new IllegalStateException("Flat number must be positive");
        }

        if (personalData == null) {
            throw new IllegalStateException("Personal data cannot be null");
        }

        if (registrationStatus == null) {
            throw  new IllegalStateException("Registration status cannot be null");
        }
    }

    public Integer getId() { return id; }

    public Boolean getActive() {return active; }

    public void setActive(Boolean active) {
        if (active == null) {
            throw new IllegalArgumentException("Active status cannot be null");
        }
        this.active = active;
    }

    public LocalDate getDate() { return date; }

    public String getPostalIndex() { return postalIndex; }

    public String getMigrationDepartment() { return migrationDepartment; }

    public String getRegion() { return region; }

    public String getDistrict() { return district; }

    public String getCity() { return city; }

    public String getStreet() { return street; }

    public String getHouse() { return house; }

    public Integer getFlat() { return flat; }

    public PersonalDataEntity getPersonalData() { return personalData; }

    public RegistrationStatus getRegistrationStatus() { return registrationStatus; }
}
