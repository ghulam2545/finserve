package com.ghulam.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventContainer<T> {

    private static final JsonMapper OBJECT_MAPPER = JsonMapper.builder()
            .findAndAddModules()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    private String eventType;

    @Builder.Default
    private int schemaVersion = 1;

    @Builder.Default
    private Instant occurredAt = Instant.now();

    private String correlationId;

    private String producerService;

    private T payload;

    public static <T> EventContainer<T> wrap(
            String eventType,
            T payload,
            String correlationId,
            String producerService) {

        return EventContainer.<T>builder()
                .eventType(eventType)
                .payload(payload)
                .correlationId(correlationId)
                .producerService(producerService)
                .build();
    }

    public static EventContainer<?> from(Object recordValue) {
        if (recordValue == null) {
            return null;
        }
        if (recordValue instanceof EventContainer<?>) {
            return (EventContainer<?>) recordValue;
        }
        return OBJECT_MAPPER.convertValue(recordValue, EventContainer.class);
    }

    @SuppressWarnings("unchecked")
    public <P> P getPayloadAs(Class<P> targetClass) {
        if (payload == null) {
            return null;
        }
        if (targetClass.isInstance(payload)) {
            return (P) payload;
        }
        return OBJECT_MAPPER.convertValue(payload, targetClass);
    }
}
