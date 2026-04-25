package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "mail", nullable = false, length = 254, unique = true)
    private String mail;

    @Column(name = "password_hash", nullable = false, length = 64)
    private String passwordHash;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonalDataEntity> personalData = new ArrayList<>();

    protected UserEntity() {
    }

    public UserEntity(String username,
                      String mail,
                      String passwordHash,
                      Boolean active,
                      Role role) {
        setUsername(username);
        setMail(mail);
        setPasswordHash(passwordHash);
        setActive(active);
        setRole(role);
    }

    public void addPersonalData(PersonalDataEntity personalData) {
        if (personalData == null) {
            throw new IllegalArgumentException("Personal data cannot be null");
        }
        this.personalData.add(personalData);
        personalData.setUser(this);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = role;
    }

    @PrePersist
    @PreUpdate
    private void prePersistAndUpdate() {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalStateException("Username cannot be empty");
        }
        if (username.length() > 50) {
            throw new IllegalStateException("Username must be less than 50 characters");
        }

        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalStateException("Mail cannot be empty");
        }
        if (!mail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalStateException("Invalid mail format");
        }
        if (mail.length() > 254) {
            throw new IllegalStateException("Mail must be less than 254 characters");
        }

        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalStateException("Password hash cannot be empty");
        }

        if (active == null) {
            throw new IllegalStateException("Active status cannot be null");
        }

        if (role == null) {
            throw new IllegalStateException("Role cannot be null");
        }
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("Username must be less than 50 characters");
        }
        this.username = username.trim();
    }

    public void setMail(String mail) {
        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalArgumentException("Mail cannot be empty");
        }
        if (!mail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid mail format");
        }
        if (mail.length() > 254) {
            throw new IllegalArgumentException("Mail must be less than 254 characters");
        }
        this.mail = mail.trim();
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
        this.passwordHash = passwordHash;
    }


    public void setActive(Boolean active) {
        if (active == null) {
            throw new IllegalArgumentException("Active status cannot be null");
        }
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Boolean getActive() {
        return active;
    }

    public List<PersonalDataEntity> getPersonalData() {
        return personalData;
    }
}