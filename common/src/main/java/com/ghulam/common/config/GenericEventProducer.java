package com.ghulam.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

@RequiredArgsConstructor
public class GenericEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * @param topic           Kafka topic name
     * @param eventType       Logical event name (e.g. "user.created")
     * @param payload         Domain object that becomes {@code EventContainer.payload}
     * @param correlationId   Optional correlation id (null → random UUID)
     * @param producerService Name of the service that produces the event (null → “unknown-service”)
     */
    public void send(String topic,
                     String eventType,
                     Object payload,
                     String correlationId,
                     String producerService) {

        String corrId = (correlationId != null) ? correlationId : UUID.randomUUID().toString();

        EventContainer<Object> envelope = EventContainer.wrap(
                eventType,
                payload,
                corrId,
                producerService
        );

        // Use the generated eventId as the Kafka key (optional)
        kafkaTemplate.send(topic, envelope.getEventId(), envelope);
    }

    /**
     * Overload when you don’t need correlation‑id or producer‑service information.
     */
    public void send(String topic, String eventType, Object payload) {
        send(topic, eventType, payload, null, null);
    }
}