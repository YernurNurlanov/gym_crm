package com.crm.gym.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(2)
public class RequestResponseLoggingFilter
        extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    RequestResponseLoggingFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        return uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper(request);

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        try {

            filterChain.doFilter(
                    requestWrapper,
                    responseWrapper);

        } finally {

            long executionTime =
                    System.currentTimeMillis() - startTime;

            logRequest(requestWrapper);
            logResponse(
                    responseWrapper,
                    executionTime);

            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(
            ContentCachingRequestWrapper request) {

        String requestBody =
                new String(
                        request.getContentAsByteArray(),
                        StandardCharsets.UTF_8);

        requestBody = maskSensitiveData(requestBody);

        logger.info(
                """
                REQUEST
                Method={}
                URI={}
                Query={}
                Body={}
                """,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                requestBody
        );
    }

    private void logResponse(
            ContentCachingResponseWrapper response,
            long executionTime) throws IOException {

        String responseBody =
                new String(
                        response.getContentAsByteArray(),
                        StandardCharsets.UTF_8);

        responseBody = maskSensitiveData(responseBody);

        logger.info(
                """
                RESPONSE
                Status={}
                ExecutionTime={}ms
                Body={}
                """,
                response.getStatus(),
                executionTime,
                responseBody
        );
    }

    private String maskSensitiveData(String body) {

        return body.replaceAll(
                "\"password\"\\s*:\\s*\"[^\"]+\"",
                "\"password\":\"***\"");
    }
}