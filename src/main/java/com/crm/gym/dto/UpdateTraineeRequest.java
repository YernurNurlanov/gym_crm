package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class UpdateTraineeRequest {

    @NotEmpty(message = "Firstname can not be empty")
    private String fName;

    @NotEmpty(message = "Lastname can not be empty")
    private String lName;

    @NotEmpty(message = "Username can not be empty")
    private String username;

    private Date dateOfBirth;
    private String address;

    @NotNull(message = "Active field can not be empty")
    private boolean active;

    public @NotEmpty(message = "Firstname can not be empty") String getFName() {
        return fName;
    }

    public @NotEmpty(message = "Lastname can not be empty") String getlName() {
        return lName;
    }

    public @NotEmpty(message = "Username can not be empty") String getUsername() {
        return username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    @NotNull(message = "Active field can not be empty")
    public boolean isActive() {
        return active;
    }
}
