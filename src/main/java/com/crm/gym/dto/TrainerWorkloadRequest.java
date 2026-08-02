package com.crm.gym.dto;

import com.crm.gym.entity.ActionType;

import java.util.Date;

public class TrainerWorkloadRequest {
    private String username;

    private String firstName;

    private String lastName;

    private boolean active;

    private Date trainingDate;

    private Integer trainingDuration;

    private ActionType actionType;

    public TrainerWorkloadRequest(String username, String firstName, String lastName, boolean active,
                                  Date trainingDate, Integer trainingDuration, ActionType actionType) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.actionType = actionType;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
