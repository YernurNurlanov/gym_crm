package com.crm.gym.config;

import com.crm.gym.service.JwtService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignSecurityConfig {

    @Bean
    public RequestInterceptor jwtInterceptor(
            JwtService jwtService) {

        return template ->

                template.header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwtService.generateServiceToken());
    }

}
