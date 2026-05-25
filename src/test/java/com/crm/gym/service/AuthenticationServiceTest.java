package com.crm.gym.service;

import com.crm.gym.config.SecurityContext;
import com.crm.gym.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private SecurityContext securityContext;

    private EntityManager em;

    private TypedQuery<User> typedQuery;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {

        securityContext = mock(SecurityContext.class);

        em = mock(EntityManager.class);

        typedQuery = mock(TypedQuery.class);

        authenticationService = new AuthenticationService(securityContext, em);
    }

    @Test
    void shouldAuthenticateValidUser() {

        User user = new User();

        user.setUsername("test.user");

        user.setPassword("securePassword");

        when(em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter("username", "test.user"))
                .thenReturn(typedQuery);

        when(typedQuery.getSingleResult())
                .thenReturn(user);

        boolean result =
                authenticationService.authenticate("test.user", "securePassword");

        assertTrue(result);

        verify(securityContext, times(1))
                .login("test.user");
    }

    @Test
    void shouldNotAuthenticateWithWrongPassword() {

        User user = new User();

        user.setUsername("test.user");

        user.setPassword("securePassword");

        when(em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter("username", "test.user"))
                .thenReturn(typedQuery);

        when(typedQuery.getSingleResult())
                .thenReturn(user);

        boolean result =
                authenticationService.authenticate("test.user", "wrongPassword");

        assertFalse(result);

        verify(securityContext, never())
                .login(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter("username", "unknown.user"))
                .thenReturn(typedQuery);

        when(typedQuery.getSingleResult())
                .thenThrow(new NoResultException());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> authenticationService.authenticate("unknown.user", "password"));

        assertEquals(
                "User not found",
                exception.getMessage());

        verify(securityContext, never())
                .login(any());
    }

    @Test
    void shouldLogout() {

        authenticationService.logout();

        verify(securityContext, times(1))
                .logout();
    }
}
