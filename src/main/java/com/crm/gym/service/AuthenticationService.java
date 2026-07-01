package com.crm.gym.service;

import com.crm.gym.dto.ChangeLoginRequest;
import com.crm.gym.dto.LoginRequest;
import com.crm.gym.entity.User;
import com.crm.gym.exception.AuthenticationException;
import com.crm.gym.exception.NotFoundException;
import com.crm.gym.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final MetricsService metricsService;

    public AuthenticationService(UserRepository userRepository, MetricsService metricsService) {
        this.userRepository = userRepository;
        this.metricsService = metricsService;
    }

    @Transactional
    public ResponseEntity<Void> authenticate(LoginRequest credentials) {

        Optional<User> user = userRepository.findByUsername(credentials.getUsername());

        if (user.isPresent()) {
            if (user.get().getPassword().equals(credentials.getPassword())) {
                metricsService.loginCreated();
                return ResponseEntity.ok().build();
            }

            throw new AuthenticationException("Invalid password");
        } else {
            throw new NotFoundException("User with username " + credentials.getUsername() + " not found");
        }
    }

    @Transactional
    public ResponseEntity<Void> changePassword(ChangeLoginRequest request) {

        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if (user.isPresent()) {
            if (user.get().getPassword().equals(request.getOldPassword())) {
                user.get().setPassword(request.getNewPassword());
                userRepository.save(user.get());
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
