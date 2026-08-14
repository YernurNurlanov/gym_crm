package com.trainings.trainer.controller;

import com.trainings.trainer.dto.request.MonthWorkloadRequest;
import com.trainings.trainer.entity.MonthSummary;
import com.trainings.trainer.service.TrainerWorkloadService;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
@RequestMapping("/workloads")
public class TrainerWorkloadController {

    private final TrainerWorkloadService service;

    public TrainerWorkloadController(TrainerWorkloadService service) {
        this.service = service;
    }

    @GetMapping
    public MonthSummary getMonthWorkload(MonthWorkloadRequest request) {
        return service.getWorkload(request.getUsername(), request.getYear(), Month.of(request.getMonth()));
    }
}
