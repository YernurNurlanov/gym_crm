package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TrainerRegistrationRequest {

    @NotEmpty(message = "Firstname can not be empty")
    private String firstName;

    @NotEmpty(message = "Lastname can not be empty")
    private String lastName;

    @NotNull(message = "Specialization can not be empty")
    private Long specializationId;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getSpecializationId() {
        return specializationId;
    }
}

