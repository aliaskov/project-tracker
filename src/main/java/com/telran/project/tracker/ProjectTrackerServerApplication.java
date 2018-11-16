package com.telran.project.tracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@Slf4j
@SpringBootApplication
public class ProjectTrackerServerApplication {

    private static final String DELAY_ENV_VARIABLE_NAME = "START_DELAY";
    private static final Long DELAY_DEFAULT = 500L;

    public static void main(String[] args) throws InterruptedException {
        log.info("Starting Project-Tracker application");

        log.info("Delay value is {}present in env variable START_DELAY", System.getenv(DELAY_ENV_VARIABLE_NAME) == null ? "NOT " : "");

        Long delay = Optional
                .ofNullable(System.getenv(DELAY_ENV_VARIABLE_NAME))
                .map(x -> {
                    try {
                        Long.valueOf(x);
                    } catch (NumberFormatException e) {
                        log.error("START_DELAY env variable must be digit");
                        throw new RuntimeException("START_DELAY env variable must be digit", e);
                    }
                    return Long.valueOf(x);
                })
                .orElse(DELAY_DEFAULT);
        log.info("Delay is {}", delay);

        Thread.sleep(delay);
        SpringApplication.run(ProjectTrackerServerApplication.class, args);
    }
}
