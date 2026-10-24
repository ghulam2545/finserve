package com.ghulam.customer.events;

import com.ghulam.common.config.EventContainer;
import com.ghulam.common.config.GenericEventConsumer;
import com.ghulam.common.constants.TopicConstants;
import com.ghulam.common.events.UserRegisteredEvent;
import com.ghulam.customer.entities.Customer;
import com.ghulam.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerRegisteredConsumer extends GenericEventConsumer<UserRegisteredEvent> {


    @Override
    protected String getTopic() {
        return TopicConstants.TOPIC_USER_REGISTERED;
    }

    @Override
    protected Class<UserRegisteredEvent> getPayloadClass() {
        return UserRegisteredEvent.class;
    }

    @Override
    protected void handle(EventContainer<UserRegisteredEvent> envelope) {
        log.info("Received event {}", envelope);
    }
}