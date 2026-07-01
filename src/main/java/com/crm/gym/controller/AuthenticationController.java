package com.crm.gym.controller;

import com.crm.gym.dto.ChangeLoginRequest;
import com.crm.gym.dto.LoginRequest;
import com.crm.gym.service.AuthenticationService;
import com.crm.gym.util.AuthRequired;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.authenticate(request);
    }

    @AuthRequired
    @PutMapping("/change-login")
    public ResponseEntity<Void> changeLogin(@RequestBody @Valid ChangeLoginRequest request) {
        return authenticationService.changePassword(request);
    }
}
