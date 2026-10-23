package com.ghulam.customer.entities;

import com.ghulam.customer.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_addresses", schema = "customer")
public class CustomerAddress {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false, length = 50)
    private AddressType addressType;

    @Column(nullable = false)
    private String line1;

    @Column
    private String line2;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 20)
    private String pincode;

    @Column(name = "country_code", nullable = false, columnDefinition = "CHAR(2)")
    private String countryCode;

    @Builder.Default
    @Column(name = "is_current", nullable = false)
    private boolean isCurrent = true;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}