package com.ghulam.customer.dtos;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record CustomerProfileResponse(
        UUID customerId,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String nationality,
        AddressDto address,
        boolean profileComplete,
        Instant createdAt,
        Instant updatedAt) {
}