package com.crm.gym.integration;

import com.crm.gym.GymApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@CucumberContextConfiguration
@SpringBootTest(
        classes = GymApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CucumberIntegrationSpringConfiguration {

        @LocalServerPort
        protected int port;
}