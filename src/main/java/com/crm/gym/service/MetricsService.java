package com.crm.gym.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricsService {

    private final Counter trainerCounter;
    private final Counter loginCounter;

    public MetricsService(MeterRegistry registry) {

        trainerCounter = Counter.builder(
                        "gym_created_trainers_total")
                .description("Created trainers")
                .register(registry);

        loginCounter = Counter.builder(
                "gym_login_users_total")
                .description("Login users")
                .register(registry);
    }

    public void trainerCreated() {
        trainerCounter.increment();
    }

    public void loginCreated() {
        loginCounter.increment();
    }

}
