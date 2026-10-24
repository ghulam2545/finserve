package com.ghulam.auth.service;

import com.ghulam.common.config.EventContainer;
import com.ghulam.common.config.GenericEventProducer;
import com.ghulam.common.constants.TopicConstants;
import com.ghulam.common.events.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEventService {

    private final GenericEventProducer producer;

    public UserEventService(GenericEventProducer producer) {
        this.producer = producer;
    }

    public void publishUserRegistered(
            UserRegisteredEvent payload,
            String correlationId) {

        EventContainer<UserRegisteredEvent> event =
                EventContainer.wrap(
                        "user.created",
                        payload,
                        correlationId,
                        "auth-service"
                );

        producer.send(
                TopicConstants.TOPIC_USER_REGISTERED,
                payload.getUserId(), // TODO
                event
        );
    }
}