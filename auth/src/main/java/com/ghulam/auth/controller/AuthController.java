package com.ghulam.auth.controller;

import com.ghulam.auth.dtos.LoginRequest;
import com.ghulam.auth.dtos.RefreshRequest;
import com.ghulam.auth.dtos.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest request) {
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(String headers) {
        return null;
    }
}