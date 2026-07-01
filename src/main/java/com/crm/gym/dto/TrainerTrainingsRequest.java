package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

public class TrainerTrainingsRequest {

    @NotEmpty(message = "Trainer username can not be empty")
    private String username;

    private Date periodFrom;
    private Date periodTo;
    private String traineeName;

    public @NotEmpty(message = "Trainer username can not be empty") String getUsername() {
        return username;
    }

    public Date getPeriodFrom() {
        return periodFrom;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    public String getTraineeName() {
        return traineeName;
    }
}
