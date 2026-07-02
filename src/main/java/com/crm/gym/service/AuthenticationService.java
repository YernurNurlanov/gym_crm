package com.crm.gym.service;

import com.crm.gym.dto.ChangeLoginRequest;
import com.crm.gym.dto.LoginRequest;
import com.crm.gym.entity.User;
import com.crm.gym.exception.AuthenticationException;
import com.crm.gym.exception.NotFoundException;
import com.crm.gym.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final MetricsService metricsService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, MetricsService metricsService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.metricsService = metricsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<Void> authenticate(LoginRequest credentials) {

        Optional<User> user = userRepository.findByUsername(credentials.getUsername());

        if (user.isPresent()) {
            if (passwordEncoder.matches(credentials.getPassword(), user.get().getPassword())) {
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
            if (passwordEncoder.matches(request.getOldPassword(), user.get().getPassword())) {
                user.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user.get());
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
