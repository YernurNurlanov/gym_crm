package com.crm.gym.controller;

import com.crm.gym.entity.TrainingType;
import com.crm.gym.service.TrainingTypeService;
import com.crm.gym.util.AuthRequired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @AuthRequired
    @GetMapping
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }
}
