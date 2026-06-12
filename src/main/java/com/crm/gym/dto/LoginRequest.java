package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginRequest {

    @NotEmpty(message = "Username con not be empty")
    private String username;

    @NotEmpty(message = "Password can not be empty")
    private String password;

    public void setUsername(@NotEmpty(message = "Username con not be empty") String username) {
        this.username = username;
    }

    public void setPassword(@NotEmpty(message = "Password can not be empty") String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
