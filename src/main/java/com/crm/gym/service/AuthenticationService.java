package com.crm.gym.service;

import com.crm.gym.entity.User;
import com.crm.gym.config.SecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final SecurityContext securityContext;

    @PersistenceContext
    private final EntityManager em;

    public AuthenticationService(SecurityContext securityContext, EntityManager em) {
        this.securityContext = securityContext;
        this.em = em;
    }

    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (user.getPassword().equals(password)) {
                securityContext.login(username);
                return true;
            }
        } catch (NoResultException e) {
            throw new RuntimeException("User not found");
        }
        return false;
    }

    public void logout() {
        securityContext.logout();
    }
}
