package com.ghulam.auth.dtos;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresInSeconds,
        String tokenType) {
}