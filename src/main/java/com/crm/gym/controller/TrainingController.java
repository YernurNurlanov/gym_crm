package com.crm.gym.controller;

import com.crm.gym.dto.AddTrainingRequest;
import com.crm.gym.entity.Training;
import com.crm.gym.service.TrainingService;
import com.crm.gym.util.AuthRequired;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @AuthRequired
    @PostMapping("training")
    public ResponseEntity<Void> addTraining(@RequestBody @Valid AddTrainingRequest request) {
        return trainingService.addTraining(request);
    }
}
