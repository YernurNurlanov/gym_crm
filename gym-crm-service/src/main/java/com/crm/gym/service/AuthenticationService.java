package com.crm.gym.service;

import com.crm.gym.dto.ChangeLoginRequest;
import com.crm.gym.dto.LoginRequest;
import com.crm.gym.dto.LoginResponse;
import com.crm.gym.entity.User;
import com.crm.gym.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import com.crm.gym.exception.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.crm.gym.exception.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final MetricsService metricsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;

    public AuthenticationService(UserRepository userRepository, MetricsService metricsService,
                                 PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                 JwtService jwtService, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.metricsService = metricsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
    }

    @Transactional
    public LoginResponse authenticate(LoginRequest credentials) {
        if (loginAttemptService.isBlocked(credentials.getUsername())) {
            throw new LockedException("User is blocked. Try again later.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword()
                    )
            );
            loginAttemptService.loginSucceeded(credentials.getUsername());

            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            metricsService.loginCreated();
            return new LoginResponse(
                    token,
                    user.getUsername()
            );

        } catch (RuntimeException ex) {
            loginAttemptService.loginFailed(credentials.getUsername());
            throw new AuthenticationException("Invalid username or password");
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
