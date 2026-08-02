package com.crm.gym.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(5);

    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> blockedUsers = new ConcurrentHashMap<>();

    public boolean isBlocked(String username) {

        LocalDateTime blockedUntil = blockedUsers.get(username);

        if (blockedUntil == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(blockedUntil)) {
            blockedUsers.remove(username);
            attempts.remove(username);
            return false;
        }

        return true;
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
        blockedUsers.remove(username);
    }

    public void loginFailed(String username) {

        int currentAttempts =
                attempts.getOrDefault(username, 0) + 1;

        attempts.put(username, currentAttempts);

        if (currentAttempts >= MAX_ATTEMPTS) {
            blockedUsers.put(
                    username,
                    LocalDateTime.now().plus(BLOCK_DURATION));
        }
    }

    public long getRemainingBlockSeconds(String username) {

        LocalDateTime until = blockedUsers.get(username);

        if (until == null) {
            return 0;
        }

        return Duration.between(
                LocalDateTime.now(),
                until).toSeconds();
    }
}