package com.crm.gym.dto;

import java.util.Date;

public class TrainingDTO {

    private String trainingName;
    private Date trainingDate;
    private String trainingType;
    private Integer trainingDuration;
    private String username;

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public void setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public String getUsername() {
        return username;
    }
}
