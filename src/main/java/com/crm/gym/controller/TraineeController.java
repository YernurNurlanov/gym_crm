package com.crm.gym.controller;

import com.crm.gym.dto.*;
import com.crm.gym.service.TraineeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping("/register")
    public RegistrationResponse registerTrainee(@RequestBody @Valid TraineeRegistrationRequest request) {
        return traineeService.createTrainee(request);
    }

    @GetMapping()
    public TraineeDTO getTrainee(@RequestParam String username) {
        return traineeService.getTrainee(username);
    }

    @PutMapping()
    public UpdateTraineeResponse updateTrainee(@RequestBody @Valid UpdateTraineeRequest request) {
        return traineeService.updateTrainee(request);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteTrainee(@RequestParam String username) {
        return traineeService.deleteTrainee(username);
    }

    @PostMapping("/trainings")
    public List<TrainingDTO> getTraineeTrainings(@RequestBody @Valid TraineeTrainingsRequest request) {
        return traineeService.getTraineeTrainings(request);
    }

    @PatchMapping("/status")
    public ResponseEntity<Void> setTraineeStatus(@RequestBody @Valid UserIsActiveRequest request) {
        return traineeService.setTraineeStatus(request);
    }

}
