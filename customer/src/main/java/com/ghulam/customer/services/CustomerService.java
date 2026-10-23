package com.ghulam.customer.services;

import com.ghulam.customer.dtos.AddressDto;
import com.ghulam.customer.dtos.CustomerProfileResponse;
import com.ghulam.customer.entities.Customer;
import com.ghulam.customer.entities.CustomerAddress;
import com.ghulam.customer.repo.CustomerAddressRepository;
import com.ghulam.customer.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;

    @Transactional
    public Customer createCustomerProfile(UUID userId, String firstName, String lastName) {
        Optional<Customer> existing = customerRepository.findById(userId);
        if (existing.isPresent()) {
            log.info("Customer profile already exists for userId: {}", userId);
            return existing.get();
        }

        Instant now = Instant.now();
        Customer customer = Customer.builder()
                .id(userId)
                .firstName(firstName)
                .lastName(lastName)
                .profileComplete(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        customer = customerRepository.save(customer);
        log.info("Customer profile created for userId: {}", userId);
        return customer;
    }

    @Transactional(readOnly = true)
    public CustomerProfileResponse getCustomerById(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer: " + customerId));

        Optional<CustomerAddress> addressOpt = addressRepository.findByCustomerIdAndIsCurrentTrue(customerId);
        AddressDto addressDto = addressOpt.map(this::toAddressDto).orElse(null);

        return toCustomerProfileResponse(customer, addressDto);
    }

    private AddressDto toAddressDto(CustomerAddress address) {
        return AddressDto.builder()
                .line1(address.getLine1())
                .line2(address.getLine2())
                .city(address.getCity())
                .pincode(address.getPincode())
                .countryCode(address.getCountryCode())
                .build();
    }

    private CustomerProfileResponse toCustomerProfileResponse(Customer customer, AddressDto addressDto) {
        return CustomerProfileResponse.builder()
                .customerId(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(customer.getDateOfBirth())
                .nationality(customer.getNationality())
                .address(addressDto)
                .profileComplete(customer.isProfileComplete())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
