package com.crm.gym.service;

import com.crm.gym.controller.TrainerWorkloadClient;
import com.crm.gym.dto.TrainerWorkloadRequest;
import com.crm.gym.exception.TrainerWorkloadUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TrainerWorkloadService {

    private static final Logger logger =
            LoggerFactory.getLogger(TrainerWorkloadService.class);

    private final TrainerWorkloadClient trainerWorkloadClient;

    public TrainerWorkloadService(TrainerWorkloadClient trainerWorkloadClient) {
        this.trainerWorkloadClient = trainerWorkloadClient;
    }

    @Retry(name = "trainer-workload")
    @CircuitBreaker(
            name = "trainer-workload",
            fallbackMethod = "fallback")
    public void updateTrainerWorkload(TrainerWorkloadRequest request) {

        trainerWorkloadClient.updateTrainerWorkload(request);
    }

    public void fallback(TrainerWorkloadRequest request, Throwable throwable) {

        logger.error(
                "Unable to update workload for trainer {}",
                request.getUsername(),
                throwable);

        throw new TrainerWorkloadUnavailableException("Trainer Workload Service is unavailable.");
    }
}