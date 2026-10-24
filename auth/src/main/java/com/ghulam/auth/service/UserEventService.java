package com.ghulam.auth.service;

import com.ghulam.common.constants.TopicConstants;
import com.ghulam.common.config.KafkaEventPublisher;
import com.ghulam.common.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventService {

    private final KafkaEventPublisher kafkaEventPublisher;

    public void publishUserRegistered(UserRegisteredEvent payload) {
        kafkaEventPublisher.send(
                TopicConstants.TOPIC_USER_REGISTERED,
                "user.registered",
                payload
        );
    }
}