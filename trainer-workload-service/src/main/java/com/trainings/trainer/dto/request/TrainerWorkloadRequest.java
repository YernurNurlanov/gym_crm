package com.trainings.trainer.dto.request;

import com.trainings.trainer.entity.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;


public class TrainerWorkloadRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private boolean active;

    @NotNull
    private Date trainingDate;

    @Positive
    private Integer trainingDuration;

    @NotNull
    private ActionType actionType;

    public @NotBlank String getUsername() {
        return username;
    }

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    public @NotNull Date getTrainingDate() {
        return trainingDate;
    }

    public @Positive Integer getTrainingDuration() {
        return trainingDuration;
    }

    public @NotNull ActionType getActionType() {
        return actionType;
    }
}
