package com.crm.gym.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class TransactionIdFilter extends OncePerRequestFilter {

    public static final String TRANSACTION_ID =
            "transactionId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String transactionId =
                UUID.randomUUID().toString();

        MDC.put(
                TRANSACTION_ID,
                transactionId);

        response.setHeader(
                "Transaction-Id",
                transactionId);

        try {
            filterChain.doFilter(
                    request,
                    response);
        } finally {
            MDC.remove(TransactionIdFilter.TRANSACTION_ID);
        }
    }
}
