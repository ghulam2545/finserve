package com.ghulam.customer.events;

import com.ghulam.customer.entities.Customer;
import com.ghulam.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final CustomerService customerService;

    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        log.info("Received UserRegisteredEvent for userId: {}", event.getUserId());
        try {
            UUID userId = UUID.fromString(event.getUserId());
            Customer customer = customerService.createCustomerProfile(userId, event.getFirstName(), event.getLastName());
            log.info("Successfully handled UserRegisteredEvent for customer: {}", customer.getId());
        } catch (Exception e) {
            log.error("Failed to process UserRegisteredEvent for userId: {}", event.getUserId(), e);
            throw e;
        }
    }
}