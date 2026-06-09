package com.crm.gym.service;

import com.crm.gym.entity.User;
import com.crm.gym.config.SecurityContext;
import com.crm.gym.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final SecurityContext securityContext;
    private final UserRepository userRepository;

    public AuthenticationService(SecurityContext securityContext, UserRepository userRepository) {
        this.securityContext = securityContext;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                securityContext.login(username);
                return true;
            }
        }

        return false;
    }

    public void logout() {
        securityContext.logout();
    }
}
