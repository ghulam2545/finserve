package com.ghulam.customer.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers", schema = "customer")
public class Customer {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "nationality", length = 2)
    private String nationality;

    @Builder.Default
    @Column(name = "profile_complete", nullable = false)
    private boolean profileComplete = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Transient
    private Boolean hasAddress;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
        if (hasAddress != null) {
            this.profileComplete = isCompleteWithAddress(this.hasAddress);
        }
    }

    /**
     * Profile is complete when first name, last name, date of birth, nationality,
     * and a valid address are all present.
     */
    public boolean isCompleteWithAddress(boolean addressExists) {
        return firstName != null && !firstName.isBlank()
                && lastName != null && !lastName.isBlank()
                && dateOfBirth != null
                && nationality != null && !nationality.isBlank()
                && addressExists;
    }
}