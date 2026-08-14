package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserIsActiveRequest {

    @NotEmpty(message = "Username can not be empty")
    private String username;

    @NotNull(message = "Active field can not be empty")
    private boolean active;

    public @NotEmpty(message = "Username can not be empty") String getUsername() {
        return username;
    }

    @NotNull(message = "Active field can not be empty")
    public boolean isActive() {
        return active;
    }
}
