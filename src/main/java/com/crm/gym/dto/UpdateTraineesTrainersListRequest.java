package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UpdateTraineesTrainersListRequest {

    @NotEmpty(message = "Trainee username can not be empty")
    private String traineeUsername;

    @NotEmpty(message = "Trainers list can not be empty")
    private List<String> trainers;

    public @NotEmpty(message = "Trainee username can not be empty") String getTraineeUsername() {
        return traineeUsername;
    }

    public @NotEmpty(message = "Trainers list can not be empty") List<String> getTrainers() {
        return trainers;
    }
}
