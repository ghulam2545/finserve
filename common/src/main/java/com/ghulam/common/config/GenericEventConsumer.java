package com.ghulam.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public abstract class GenericEventConsumer<T> {

    protected abstract String getTopic();

    protected abstract Class<T> getPayloadClass();

    @KafkaListener(
            topics = "#{__listener.topic}",
            containerFactory = "kafkaListenerContainerFactory",
            errorHandler = "kafkaErrorHandler")
    public void onMessage(ConsumerRecord<String, EventContainer<T>> record) {
        try {
            EventContainer<T> envelope = record.value();
            handle(envelope);
        } catch (Exception ex) {
            log.error("Error processing message from topic {}: {}", getTopic(), ex.getMessage(), ex);
            // you may re‑throw, dead‑letter, etc. depending on your needs
        }
    }

    protected abstract void handle(EventContainer<T> envelope);
}