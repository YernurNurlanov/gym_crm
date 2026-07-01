package com.crm.gym.dto;

import jakarta.validation.constraints.NotEmpty;

public class ChangeLoginRequest {

    @NotEmpty(message = "Username con not be empty")
    private String username;

    @NotEmpty(message = "Old password can not be empty")
    private String oldPassword;

    @NotEmpty(message = "New password can not be empty")
    private String newPassword;

    public @NotEmpty(message = "Username con not be empty") String getUsername() {
        return username;
    }

    public @NotEmpty(message = "Old password can not be empty") String getOldPassword() {
        return oldPassword;
    }

    public @NotEmpty(message = "New password can not be empty") String getNewPassword() {
        return newPassword;
    }
}
