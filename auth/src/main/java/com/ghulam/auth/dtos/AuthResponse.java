package com.ghulam.auth.dtos;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record AuthResponse(
        UUID userId,
        String email,
        Instant createdAt
) {
}