package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

public class TraineeTrainingsRequest {

    @NotEmpty(message = "Trainee username can not be empty")
    private String username;

    private Date periodFrom;
    private Date periodTo;
    private String trainerName;
    private Long trainingTypeId;

    public @NotEmpty(message = "Trainee username can not be empty") String getUsername() {
        return username;
    }

    public Date getPeriodFrom() {
        return periodFrom;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public Long getTrainingTypeId() {
        return trainingTypeId;
    }
}
