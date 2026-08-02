package com.crm.gym.controller;

import com.crm.gym.dto.AddTrainingRequest;
import com.crm.gym.service.TrainingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping()
    public ResponseEntity<Void> addTraining(@RequestBody @Valid AddTrainingRequest request) {
        return trainingService.addTraining(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        return trainingService.deleteTraining(id);
    }
}
