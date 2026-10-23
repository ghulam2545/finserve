package com.ghulam.customer.controller;

import com.ghulam.customer.dtos.CustomerProfileResponse;
import com.ghulam.customer.dtos.UpdateProfileRequest;
import com.ghulam.customer.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
        return null;
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

}