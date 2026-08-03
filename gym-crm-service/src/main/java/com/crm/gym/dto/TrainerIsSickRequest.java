package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TrainerIsSickRequest {

    @NotEmpty(message = "Username can not be empty")
    private String username;

    @NotNull(message = "Sick field can not be empty")
    private boolean sick;

    public @NotEmpty(message = "Username can not be empty") String getUsername() {
        return username;
    }

    @NotNull(message = "Sick field can not be empty")
    public boolean isSick() {
        return sick;
    }
}
