package com.trainings.trainer.controller;

import com.trainings.trainer.dto.TrainerWorkloadRequest;
import com.trainings.trainer.service.TrainerWorkloadService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TrainerWorkloadConsumer {

    private final TrainerWorkloadService service;

    public TrainerWorkloadConsumer(
            TrainerWorkloadService service) {

        this.service = service;
    }

    @JmsListener(destination = "trainer-workload-queue")
    public void receive(TrainerWorkloadRequest request) {

        service.updateWorkload(request);
    }
}
