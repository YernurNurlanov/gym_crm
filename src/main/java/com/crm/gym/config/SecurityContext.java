package com.crm.gym.config;

import org.springframework.stereotype.Component;

@Component
public class SecurityContext {
    private String currentUsername;
    private boolean isAuthenticated = false;

    public void login(String username) {
        this.currentUsername = username;
        this.isAuthenticated = true;
    }

    public void logout() {
        this.currentUsername = null;
        this.isAuthenticated = false;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
