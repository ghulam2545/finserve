package com.ghulam.auth.service;

import com.ghulam.auth.dtos.AuthResponse;
import com.ghulam.auth.dtos.RegisterRequest;
import com.ghulam.auth.entities.User;
import com.ghulam.auth.entities.UserRole;
import com.ghulam.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        user = userRepository.save(user);
        log.info("User registered successfully with ID: {}", user.getId());

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
