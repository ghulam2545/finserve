package com.ghulam.customer.services;

import com.ghulam.common.constants.TopicConstants;
import com.ghulam.common.config.EventContainer;
import com.ghulam.common.events.UserRegisteredEvent;
import com.ghulam.common.logger.SimpleLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerRegisteredConsumer {

    private final CustomerService customerService;
    private final SimpleLogger log = SimpleLogger.getLogger(CustomerService.class);

    @KafkaListener(topics = TopicConstants.TOPIC_USER_REGISTERED)
    public void onUserRegistered(ConsumerRecord<String, Object> record) {
        try {
            EventContainer<?> envelope = EventContainer.from(record.value());
            if (envelope == null || envelope.getPayload() == null) {
                log.warn("Received empty or null payload from topic [{}] key [{}]", record.topic(), record.key());
                return;
            }

            UserRegisteredEvent event = envelope.getPayloadAs(UserRegisteredEvent.class);
            if (event == null) {
                log.warn("Unable to convert payload to UserRegisteredEvent: {}", envelope.getPayload());
                return;
            }

            log.info("Received UserRegisteredEvent: userId={}, email={}", event.getUserId(), event.getEmail());

            UUID userId = UUID.fromString(event.getUserId());
            customerService.createCustomerProfile(userId, event.getFirstName(), event.getLastName());
            log.info("Successfully processed UserRegisteredEvent for customer: {}", userId);

        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID in UserRegisteredEvent payload: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error processing UserRegisteredEvent from topic {}: {}", record.topic(), e.getMessage(), e);
        }
    }
}