package com.crm.gym.util;

import com.crm.gym.dto.LoginRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.crm.gym.exception.AuthenticationException;
import com.crm.gym.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Aspect
@Component
public class SecurityAspect {

    private final AuthenticationService authenticationService;

    public SecurityAspect(
            AuthenticationService authenticationService) {

        this.authenticationService =
                authenticationService;
    }

    @Before("@annotation(AuthRequired)")
    public void authenticateRequest() {

        String base64Credentials = getString();

        String credentials =
                new String(
                        Base64.getDecoder()
                                .decode(base64Credentials),
                        StandardCharsets.UTF_8);

        String[] values =
                credentials.split(":", 2);

        if (values.length != 2) {
            throw new AuthenticationException(
                    "Invalid Authorization header");
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(values[0]);
        loginRequest.setPassword(values[1]);

        authenticationService.authenticate(loginRequest);
    }

    private static String getString() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes)
                        RequestContextHolder
                                .getRequestAttributes();

        assert attributes != null;
        HttpServletRequest request =
                attributes.getRequest();

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Basic ")) {

            throw new AuthenticationException(
                    "Missing Authorization header");
        }

        return authHeader.substring(6);
    }
}