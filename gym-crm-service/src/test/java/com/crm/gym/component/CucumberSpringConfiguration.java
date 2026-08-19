package com.crm.gym.component;

import com.crm.gym.GymApplication;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.repository.UserRepository;
import com.crm.gym.service.AuthenticationService;
import com.crm.gym.service.TraineeService;
import com.crm.gym.service.TrainerService;
import com.crm.gym.service.TrainingService;
import com.crm.gym.service.TrainingTypeService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

@CucumberContextConfiguration
@SpringBootTest(
        classes = GymApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class CucumberSpringConfiguration {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TraineeRepository traineeRepository;

    @MockBean
    private TrainerRepository trainerRepository;


    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private TraineeService traineeService;

    @MockBean
    private TrainingService trainingService;

    @MockBean
    private TrainingTypeService trainingTypeService;
}