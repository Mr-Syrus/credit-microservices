package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "personal_data")
public class PersonalDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "rosfinmonitoring_status", nullable = false)
    private RosfinmonitoringStatus rosfinmonitoringStatus = RosfinmonitoringStatus.NOT_RESTRICTED;

    // паспортные данные
    @Column(name = "passport_series", nullable = false, length = 4)
    private String passportSeries;

    @Column(name = "passport_number", nullable = false, length = 6)
    private String passportNumber;

    @Column(name = "passport_issued_by", nullable = false, columnDefinition = "TEXT")
    private String passportIssuedBy;

    @Column(name = "department_code", nullable = false, length = 6)
    private String departmentCode;

    @Column(name = "passport_issue_date", nullable = false)
    private LocalDate passportIssueDate;

    // личностные данные
    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName;

    @Column(name = "middle_name", length = 64)
    private String middleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GenderStatus gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "inn", nullable = false, length = 12)
    private String inn;

    @Column(name = "snils", nullable = false, length = 11)
    private String snils;

    @Column(name = "phone", nullable = false, length = 11, unique = true)
    private String phone;

    public PersonalDataEntity() {
    }

    public PersonalDataEntity(
            UserEntity user,
            RosfinmonitoringStatus rosfinmonitoringStatus,
            String passportSeries,
            String passportNumber,
            String passportIssuedBy,
            String departmentCode,
            LocalDate passportIssueDate,
            String firstName,
            String lastName,
            String middleName,
            GenderStatus gender,
            LocalDate birthDate,
            String inn,
            String snils,
            String phone
    ) {
        this.user = user;
        this.rosfinmonitoringStatus = rosfinmonitoringStatus != null ? rosfinmonitoringStatus : RosfinmonitoringStatus.NOT_RESTRICTED;
        this.passportSeries = validateAndCleanPassportSeries(passportSeries);
        this.passportNumber = validateAndCleanPassportNumber(passportNumber);
        this.passportIssuedBy = requireNonBlank(passportIssuedBy, "Passport issued by");
        this.departmentCode = validateAndCleanDepartmentCode(departmentCode);
        this.passportIssueDate = Objects.requireNonNull(passportIssueDate, "Passport issue date cannot be null");
        this.firstName = requireNonBlank(firstName, "First name");
        this.lastName = requireNonBlank(lastName, "Last name");
        this.middleName = middleName != null && !middleName.isBlank() ? middleName : null;
        this.gender = Objects.requireNonNull(gender, "Gender cannot be null");
        this.birthDate = Objects.requireNonNull(birthDate, "Birth date cannot be null");
        this.inn = validateAndCleanInn(inn);
        this.snils = validateAndCleanSnils(snils);
        this.phone = validateAndCleanPhone(phone);
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
        return value;
    }

    private static String validateAndCleanPassportSeries(String rawSeries) {
        if (rawSeries == null) {
            throw new IllegalArgumentException("Passport series cannot be null");
        }
        String cleaned = rawSeries.replaceAll("[-\\s]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Passport series cannot be empty after removing delimiters");
        }
        if (cleaned.length() != 4) {
            throw new IllegalArgumentException("Passport series must be exactly 4 digits, but got: " + cleaned.length());
        }
        if (!cleaned.matches("\\d+")) {
            throw new IllegalArgumentException("Passport series must contain only digits, but got: " + cleaned);
        }
        return cleaned;
    }

    private static String validateAndCleanPassportNumber(String rawNumber) {
        if (rawNumber == null) {
            throw new IllegalArgumentException("Passport number cannot be null");
        }
        String cleaned = rawNumber.replaceAll("[-\\s]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Passport number cannot be empty after removing delimiters");
        }
        if (cleaned.length() != 6) {
            throw new IllegalArgumentException("Passport number must be exactly 6 digits, but got: " + cleaned.length());
        }
        if (!cleaned.matches("\\d+")) {
            throw new IllegalArgumentException("Passport number must contain only digits, but got: " + cleaned);
        }
        return cleaned;
    }

    private static String validateAndCleanDepartmentCode(String rawCode) {
        if (rawCode == null) {
            throw new IllegalArgumentException("Department code cannot be null");
        }
        String cleaned = rawCode.replaceAll("[-\\s]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Department code cannot be empty after removing delimiters");
        }
        if (cleaned.length() != 6 && cleaned.length() != 7) {
            throw new IllegalArgumentException("Department code must be 6 or 7 digits, but got: " + cleaned.length());
        }
        if (!cleaned.matches("\\d+")) {
            throw new IllegalArgumentException("Department code must contain only digits, but got: " + cleaned);
        }
        return cleaned;
    }

    private static String validateAndCleanInn(String rawInn) {
        if (rawInn == null) {
            throw new IllegalArgumentException("INN cannot be null");
        }
        String cleaned = rawInn.replaceAll("[-\\s]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("INN cannot be empty after removing delimiters");
        }
        if (cleaned.length() != 12) {
            throw new IllegalArgumentException("INN must be exactly 12 digits, but got: " + cleaned.length());
        }
        if (!cleaned.matches("\\d+")) {
            throw new IllegalArgumentException("INN must contain only digits, but got: " + cleaned);
        }
        return cleaned;
    }

    private static String validateAndCleanSnils(String rawSnils) {
        if (rawSnils == null) {
            throw new IllegalArgumentException("SNILS cannot be null");
        }
        String cleaned = rawSnils.replaceAll("[-\\s]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("SNILS cannot be empty after removing delimiters");
        }
        if (cleaned.length() != 11) {
            throw new IllegalArgumentException("SNILS must be exactly 11 digits, but got: " + cleaned.length());
        }
        if (!cleaned.matches("\\d+")) {
            throw new IllegalArgumentException("SNILS must contain only digits, but got: " + cleaned);
        }
        return cleaned;
    }

    private static String validateAndCleanPhone(String rawPhone) {
        if (rawPhone == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        // Удаляем все нецифровые символы
        String cleaned = rawPhone.replaceAll("[^\\d]", "");
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty after removing delimiters");
        }
        // Российский номер должен содержать 11 цифр
        if (cleaned.length() != 11) {
            throw new IllegalArgumentException("Russian phone number must contain exactly 11 digits, but got: " + cleaned.length());
        }
        // Допустимые первые цифры: 7 или 8
        char firstDigit = cleaned.charAt(0);
        if (firstDigit != '7' && firstDigit != '8') {
            throw new IllegalArgumentException("Russian phone number must start with 7 or 8, but got: " + firstDigit);
        }
        // Приводим к единому формату: заменяем начальную 8 на 7
        if (firstDigit == '8') {
            cleaned = "7" + cleaned.substring(1);
        }
        return cleaned;
    }

    @PrePersist
    private void prePersist() {
        if (rosfinmonitoringStatus == null) {
            throw new IllegalStateException("Rosfinmonitoring status cannot be null");
        }
        if (passportSeries == null) {
            throw new IllegalStateException("Passport series cannot be null");
        }
        if (passportSeries.length() != 4) {
            throw new IllegalStateException("Passport series must be exactly 4 digits");
        }
        if (!passportSeries.matches("\\d+")) {
            throw new IllegalStateException("Passport series must contain only digits");
        }
        if (passportNumber == null) {
            throw new IllegalStateException("Passport number cannot be null");
        }
        if (passportNumber.length() != 6) {
            throw new IllegalStateException("Passport number must be exactly 6 digits");
        }
        if (!passportNumber.matches("\\d+")) {
            throw new IllegalStateException("Passport number must contain only digits");
        }
        if (passportIssuedBy == null || passportIssuedBy.isBlank()) {
            throw new IllegalStateException("Passport issued by cannot be null or blank");
        }
        if (departmentCode == null) {
            throw new IllegalStateException("Department code cannot be null");
        }
        if (departmentCode.length() != 6 && departmentCode.length() != 7) {
            throw new IllegalStateException("Department code must be 6 or 7 digits");
        }
        if (!departmentCode.matches("\\d+")) {
            throw new IllegalStateException("Department code must contain only digits");
        }
        if (passportIssueDate == null) {
            throw new IllegalStateException("Passport issue date cannot be null");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalStateException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalStateException("Last name cannot be null or blank");
        }
        if (gender == null) {
            throw new IllegalStateException("Gender cannot be null");
        }
        if (birthDate == null) {
            throw new IllegalStateException("Birth date cannot be null");
        }
        if (inn == null) {
            throw new IllegalStateException("INN cannot be null");
        }
        if (inn.length() != 12) {
            throw new IllegalStateException("INN must be exactly 12 digits");
        }
        if (!inn.matches("\\d+")) {
            throw new IllegalStateException("INN must contain only digits");
        }
        if (snils == null) {
            throw new IllegalStateException("SNILS cannot be null");
        }
        if (snils.length() != 11) {
            throw new IllegalStateException("SNILS must be exactly 11 digits");
        }
        if (!snils.matches("\\d+")) {
            throw new IllegalStateException("SNILS must contain only digits");
        }

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalStateException("Phone cannot be empty");
        }
        String cleanPhone = phone.replaceAll("[^\\d]", "");
        if (!cleanPhone.matches("\\d{11}")) {
            throw new IllegalStateException("Phone must contain 11 digits");
        }
    }

    public Integer getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
    }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public RosfinmonitoringStatus getRosfinmonitoringStatus() {
        return rosfinmonitoringStatus;
    }

    public void setRosfinmonitoringStatus(RosfinmonitoringStatus rosfinmonitoringStatus) {
        this.rosfinmonitoringStatus = rosfinmonitoringStatus;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
        String cleanPhone = phone.replaceAll("[^\\d]", "");
        if (!cleanPhone.matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone must contain 11 digits");
        }
        this.phone = cleanPhone;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getPassportIssuedBy() {
        return passportIssuedBy;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public LocalDate getPassportIssueDate() {
        return passportIssueDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public GenderStatus getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getInn() {
        return inn;
    }

    public String getSnils() {
        return snils;
    }

    public String getPhone() {return phone; }
}