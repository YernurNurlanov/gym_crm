package com.trainings.trainer.component;

import com.trainings.trainer.TrainerApplication;
import com.trainings.trainer.repository.TrainerWorkloadRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@CucumberContextConfiguration
@SpringBootTest(
        classes = TrainerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
public class CucumberSpringConfiguration {

    @MockBean
    private TrainerWorkloadRepository repository;
}