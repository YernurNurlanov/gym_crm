package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdateTrainerRequest {

    @NotEmpty(message = "Username can not be empty")
    private String username;

    @NotEmpty(message = "Firstname can not be empty")
    private String fName;

    @NotEmpty(message = "Lastname can not be empty")
    private String lName;

    @NotNull(message = "Specialization can not be empty")
    private Long specializationId;

    @NotNull(message = "Active field can not be empty")
    private boolean active;

    public @NotEmpty(message = "Username can not be empty") String getUsername() {
        return username;
    }

    public @NotEmpty(message = "Firstname can not be empty") String getFName() {
        return fName;
    }

    public @NotEmpty(message = "Lastname can not be empty") String getLName() {
        return lName;
    }

    public @NotNull(message = "Specialization can not be empty") Long getSpecializationId() {
        return specializationId;
    }

    @NotNull(message = "Active field can not be empty")
    public boolean isActive() {
        return active;
    }
}
