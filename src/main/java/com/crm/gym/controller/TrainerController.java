package com.crm.gym.controller;

import com.crm.gym.dto.*;
import com.crm.gym.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    public RegistrationResponse registerTrainer(@RequestBody @Valid TrainerRegistrationRequest request) {
        return trainerService.createTrainer(request);
    }

    @GetMapping()
    public TrainerDTO getTrainer(@RequestParam String username) {
        return trainerService.getTrainer(username);
    }

    @PutMapping("")
    public UpdateTrainerResponse updateTrainer(@RequestBody @Valid UpdateTrainerRequest request) {
        return trainerService.updateTrainer(request);
    }

    @GetMapping("/not-assigned-to-trainee")
    public List<TrainersListDTO> getTrainersNotAssignedToTrainee(@RequestParam String username) {
        return trainerService.getTrainersNotAssignedToTrainee(username);
    }

    @PutMapping("/trainee-trainers-list")
    public List<TrainersListDTO> updateTraineesTrainersList(@RequestBody @Valid UpdateTraineesTrainersListRequest request) {
        return trainerService.updateTraineesTrainersList(request);
    }

    @PostMapping("/trainings")
    public List<TrainingDTO> getTrainerTrainings(@RequestBody @Valid TrainerTrainingsRequest request) {
        return trainerService.getTrainerTrainings(request);
    }

    @PatchMapping("/status")
    public ResponseEntity<Void> setTrainerStatus(@RequestBody @Valid UserIsActiveRequest request) {
        return trainerService.setTrainerStatus(request);
    }

    @PatchMapping("/health")
    public ResponseEntity<Void> setTrainerHealth(@RequestBody @Valid TrainerIsSickRequest request) {
        return trainerService.setTrainerHealth(request);
    }
}
