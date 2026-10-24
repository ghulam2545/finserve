package com.ghulam.auth;

import com.ghulam.common.logger.SimpleLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication(scanBasePackages = "com.ghulam")
public class AuthApplication {

    private static final String DOCS_URL = "http://localhost:8081/swagger-ui/index.html";
    private static final SimpleLogger log = SimpleLogger.getLogger(AuthApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info(DOCS_URL);
    }

}
