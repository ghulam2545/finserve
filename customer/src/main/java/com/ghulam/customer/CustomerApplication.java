package com.ghulam.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class CustomerApplication {

    private static final String LOG_SEPARATOR = "──────────────────────────────────────────────────────: ";
    private static final String DOCS_URL = "http://localhost:8082/swagger-ui/index.html";

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info(LOG_SEPARATOR + "{}", DOCS_URL);
    }

}
