package com.ghulam.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String serviceName;

    /**
     * Publish a domain event to Kafka.
     *
     * @param topic     Kafka topic name (use {@link com.ghulam.common.constants.TopicConstants})
     * @param eventType logical event name, e.g. "user.registered"
     * @param payload   domain object that becomes {@code EventContainer.payload}
     */
    public void send(String topic, String eventType, Object payload) {
        EventContainer<Object> envelope = EventContainer.wrap(
                eventType,
                payload,
                UUID.randomUUID().toString(),
                serviceName
        );

        kafkaTemplate.send(topic, envelope.getEventId(), envelope);
        log.info("Published event [{}] to topic [{}] with id [{}]",
                eventType, topic, envelope.getEventId());
    }
}
