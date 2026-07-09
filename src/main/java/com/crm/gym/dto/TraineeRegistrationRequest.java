package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

public class TraineeRegistrationRequest {

    @NotEmpty(message = "Firstname can not be empty")
    private String firstName;

    @NotEmpty(message = "Lastname can not be empty")
    private String lastName;

    private Date dateOfBirth;

    private String address;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }
}
