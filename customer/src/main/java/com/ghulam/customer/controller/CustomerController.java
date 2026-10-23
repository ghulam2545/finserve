package com.ghulam.customer.controller;

import com.ghulam.customer.dtos.CustomerProfileResponse;
import com.ghulam.customer.dtos.UpdateProfileRequest;
import com.ghulam.customer.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/me")
    public ResponseEntity<CustomerProfileResponse> getMyProfile(Authentication authentication) {
        UUID userId = extractUserId(authentication);
        CustomerProfileResponse response = customerService.getCustomerById(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<CustomerProfileResponse> updateMyProfile(Authentication authentication,
                                                                   @Valid @RequestBody UpdateProfileRequest request) {
        return null;
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CustomerProfileResponse> getCustomerById(@PathVariable UUID customerId) {
        return null;
    }

    private UUID extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("User is not authenticated: 401");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UUID uuid) {
            return uuid;
        }
        try {
            return UUID.fromString(principal.toString());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid user ID in token principal: 401");
        }
    }

}