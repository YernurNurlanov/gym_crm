//package com.crm.gym.service;
//
//import com.crm.gym.config.SecurityContext;
//import com.crm.gym.entity.User;
//import com.crm.gym.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class AuthenticationServiceTest {
//
//    private SecurityContext securityContext;
//    private UserRepository userRepository;
//    private AuthenticationService authenticationService;
//
//    @BeforeEach
//    void setUp() {
//        securityContext = mock(SecurityContext.class);
//        userRepository = mock(UserRepository.class);
//
//        authenticationService = new AuthenticationService(
//                securityContext,
//                userRepository
//        );
//    }
//
//    @Test
//    void shouldAuthenticateValidUser() {
//
//        User user = new User();
//        user.setUsername("test.user");
//        user.setPassword("securePassword");
//
//        when(userRepository.findByUsername("test.user"))
//                .thenReturn(Optional.of(user));
//
//        boolean result =
//                authenticationService.authenticate("test.user", "securePassword");
//
//        assertTrue(result);
//
//        verify(securityContext).login("test.user");
//    }
//
//    @Test
//    void shouldNotAuthenticateWrongPassword() {
//
//        User user = new User();
//        user.setUsername("test.user");
//        user.setPassword("securePassword");
//
//        when(userRepository.findByUsername("test.user"))
//                .thenReturn(Optional.of(user));
//
//        boolean result =
//                authenticationService.authenticate("test.user", "wrongPassword");
//
//        assertFalse(result);
//
//        verify(securityContext, never()).login(any());
//    }
//
//    @Test
//    void shouldReturnFalseWhenUserNotFound() {
//
//        when(userRepository.findByUsername("unknown.user"))
//                .thenReturn(Optional.empty());
//
//        boolean result =
//                authenticationService.authenticate("unknown.user", "password");
//
//        assertFalse(result);
//
//        verify(securityContext, never()).login(any());
//    }
//
//    @Test
//    void shouldLogout() {
//        authenticationService.logout();
//
//        verify(securityContext).logout();
//    }
//}