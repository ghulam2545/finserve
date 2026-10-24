package com.ghulam.auth.service;

import com.ghulam.auth.dtos.AuthResponse;
import com.ghulam.auth.dtos.LoginRequest;
import com.ghulam.auth.dtos.RegisterRequest;
import com.ghulam.auth.dtos.TokenResponse;
import com.ghulam.auth.entities.RefreshToken;
import com.ghulam.auth.entities.User;
import com.ghulam.auth.entities.UserRole;
import com.ghulam.auth.repo.RefreshTokenRepository;
import com.ghulam.auth.repo.UserRepository;
import com.ghulam.auth.security.TokenService;
import com.ghulam.common.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService; // JSON Web Token service
    private final UserEventService userEventService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email();
        if (userRepository.existsByEmail(email)) {
            log.warn("Registration rejected: email already registered: {}", email);
            throw new RuntimeException("Registration rejected: email already registered.");
        }

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.CUSTOMER)
                .enabled(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        try {
            user = userRepository.save(user);
            log.info("User registered successfully with ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to register user with email {}: {}", email, e.getMessage());
            throw new RuntimeException("User registration failed, please try again later.", e);
        }

        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId().toString())
                .email(user.getEmail())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(user.getRole().name())
                .build();

        userEventService.publishUserRegistered(event,"SOMETHING");
        log.info("Published UserRegisteredEvent for user: {}", user.getId());

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        String email = request.email();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password: 401"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Authentication failed for user: {}: invalid credentials", email);
            throw new RuntimeException("Invalid email or password: 401");
        }

        if (!user.isEnabled()) {
            log.warn("Authentication failed for user: {}: account disabled", email);
            throw new RuntimeException("Account is disabled: 403");
        }

        String accessKey = UUID.randomUUID().toString();
        String refreshKey = UUID.randomUUID().toString();

        String accessToken = tokenService.generateAccessToken(
                user.getId(), user.getEmail(), user.getRole().name(), accessKey);
        String refreshToken = tokenService.generateRefreshToken(
                user.getId(), user.getEmail(), user.getRole().name(), refreshKey);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                // -> .id(UUID.randomUUID())
                .userId(user.getId())
                .tokenId(refreshKey)
                .expiresAt(tokenService.calculateRefreshTokenExpiry())
                .revoked(false)
                .createdAt(OffsetDateTime.now())
                .build();

        try {
            tokenRepository.save(refreshTokenEntity);
            log.info("Refresh token persisted for user: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to persist refresh token for user {}: {}", user.getId(), e.getMessage());
            throw new RuntimeException("Unable to store refresh token, login aborted.", e);
        }
        log.info("User logged in successfully: {}", user.getId());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresInSeconds(tokenService.getAccessTokenExpirySeconds())
                .tokenType("Bearer")
                .build();
    }
}
