package com.ghulam.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventContainer<T> {

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
}