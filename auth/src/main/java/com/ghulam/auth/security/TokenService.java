package com.ghulam.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class TokenService {

    @Value("${auth.jwt.secret}")
    private String secret;

    @Value("${auth.jwt.access-token-expiry-minutes}")
    private long accessTokenExpiryMinutes;

    @Value("${auth.jwt.refresh-token-expiry-days}")
    private long refreshTokenExpiryDays;

    @Value("${auth.jwt.issuer}")
    private String issuer;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64) {
            // Guarantee at least 64 bytes (512 bits) for HS512 algorithm
            try {
                MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
                keyBytes = sha512.digest(keyBytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("SHA-512 not available", e);
            }
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UUID userId, String email, String role, String token) {
        Instant now = Instant.now();
        Instant expiry = now.plus(Duration.ofMinutes(accessTokenExpiryMinutes));

        return Jwts.builder()
                .subject(userId.toString())
                .id(token)
                .claim("email", email)
                .claim("role", role)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(UUID userId, String email, String role, String token) {
        Instant now = Instant.now();
        Instant expiry = now.plus(Duration.ofDays(refreshTokenExpiryDays));

        return Jwts.builder()
                .subject(userId.toString())
                .id(token)
                .claim("email", email)
                .claim("role", role)
                .claim("type", "REFRESH")
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public long getAccessTokenExpirySeconds() {
        return accessTokenExpiryMinutes * 60;
    }

    public OffsetDateTime calculateRefreshTokenExpiry() {
        return OffsetDateTime.now(ZoneOffset.UTC)
                .plusDays(refreshTokenExpiryDays);
    }
}