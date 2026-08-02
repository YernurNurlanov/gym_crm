package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class AddTrainingRequest {

    @NotEmpty(message = "Trainee username can not be empty")
    private String traineeUsername;

    @NotEmpty(message = "Trainer username can not be empty")
    private String trainerUsername;

    @NotEmpty(message = "Training name can not be empty")
    private String trainingName;

    @NotNull(message = "Training date can not be empty")
    private Date trainingDate;

    @NotNull(message = "Training duration can not be empty")
    private Integer trainingDuration;

    public @NotEmpty(message = "Trainee username can not be empty") String getTraineeUsername() {
        return traineeUsername;
    }

    public @NotEmpty(message = "Trainer username can not be empty") String getTrainerUsername() {
        return trainerUsername;
    }

    public @NotEmpty(message = "Training name can not be empty") String getTrainingName() {
        return trainingName;
    }

    public @NotNull(message = "Training date can not be empty") Date getTrainingDate() {
        return trainingDate;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }
}
