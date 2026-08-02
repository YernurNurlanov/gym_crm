package com.trainings.trainer.controller;

import com.trainings.trainer.dto.request.MonthWorkloadRequest;
import com.trainings.trainer.dto.request.TrainerWorkloadRequest;
import com.trainings.trainer.entity.MonthSummary;
import com.trainings.trainer.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
@RequestMapping("/workloads")
public class TrainerWorkloadController {

    private final TrainerWorkloadService service;

    public TrainerWorkloadController(TrainerWorkloadService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> updateWorkload(@RequestBody @Valid TrainerWorkloadRequest request) {

        service.updateWorkload(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public MonthSummary getMonthWorkload(MonthWorkloadRequest request) {
        return service.getWorkload(request.getUsername(), request.getYear(), Month.of(request.getMonth()));
    }
}
