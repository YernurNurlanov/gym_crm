package com.crm.gym.util;

import com.crm.gym.config.SecurityContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {

    private final SecurityContext securityContext;

    public SecurityAspect(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Pointcut("execution(* com.crm.gym.GymFacade.*(..))")
    public void allFacadeMethods() {}

    @Pointcut("execution(* com.crm.gym.GymFacade.createTrainer(..)) " +
            "|| execution(* com.crm.gym.GymFacade.createTrainee(..)) " +
            "|| execution(* com.crm.gym.GymFacade.authenticate(..))")
    public void createProfileMethods() {}

    @Before("allFacadeMethods() && !createProfileMethods()")
    public void checkAuthentication() {
        if (!securityContext.isAuthenticated()) {
            throw new SecurityException("Authentication required! Please log in first.");
        }
        System.out.println("[Security] Check passed for user: " + securityContext.getCurrentUsername());
    }
}
