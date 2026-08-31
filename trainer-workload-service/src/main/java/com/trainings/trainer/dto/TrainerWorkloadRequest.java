package com.trainings.trainer.dto;

import com.trainings.trainer.entity.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.Date;


public class TrainerWorkloadRequest implements Serializable {

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

    public void setActionType(@NotNull ActionType actionType) {
        this.actionType = actionType;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public void setTrainingDate(@NotNull Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setTrainingDuration(@Positive Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }
}
