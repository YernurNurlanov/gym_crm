package com.crm.gym.component;

import com.crm.gym.dto.*;
import com.crm.gym.entity.TrainingType;
import com.crm.gym.exception.AuthenticationException;
import com.crm.gym.exception.NotFoundException;
import com.crm.gym.service.*;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ComponentStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingTypeService trainingTypeService;

    private String response;
    private int responseStatus;

    @Before
    public void setUp() {
        response = null;
        responseStatus = 0;
        reset(
                authenticationService,
                trainerService,
                traineeService,
                trainingService,
                trainingTypeService
        );
    }

    // ============================================================
    // AUTHENTICATION
    // ============================================================

    @Given("valid login credentials")
    public void validLoginCredentials() {
        LoginResponse loginResponse =
                new LoginResponse(
                        "test-jwt-token",
                        "john.doe"
                );

        when(authenticationService.authenticate(any(LoginRequest.class)))
                .thenReturn(loginResponse);
    }

    @When("I login with username {string} and password {string}")
    public void login(String username, String password)
            throws Exception {

        String json = """
                {
                  "username": "%s",
                  "password": "%s"
                }
                """.formatted(username, password);

        var result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int status) {
        org.junit.jupiter.api.Assertions.assertEquals(
                status,
                responseStatus
        );
    }

    @And("the response should contain username {string}")
    public void responseShouldContainUsername(String username) {
        org.junit.jupiter.api.Assertions.assertTrue(
                response.contains(username)
        );
    }

    @Given("authentication service rejects credentials")
    public void authenticationServiceRejectsCredentials() {

        when(authenticationService.authenticate(any(LoginRequest.class)))
                .thenThrow(
                        new AuthenticationException(
                                "Invalid username or password"
                        )
                );
    }

    // ============================================================
    // TRAINER
    // ============================================================

    @Given("valid trainer registration data")
    public void validTrainerRegistrationData() {

        RegistrationResponse registrationResponse =
                new RegistrationResponse();

        registrationResponse.setUsername("john.doe");
        registrationResponse.setPassword("password123");

        when(trainerService.createTrainer(
                any(TrainerRegistrationRequest.class)
        )).thenReturn(registrationResponse);
    }

    @When("I register trainer with first name {string}, last name {string} and specialization {long}")
    public void registerTrainer(
            String firstName,
            String lastName,
            long specialization
    ) throws Exception {

        String json = """
                {
                  "firstName": "%s",
                  "lastName": "%s",
                  "specializationId": %d
                }
                """.formatted(
                firstName,
                lastName,
                specialization
        );

        var result = mockMvc.perform(
                post("/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    @Given("trainer {string} exists")
    public void trainerExists(String username) {

        TrainerDTO trainerDTO = new TrainerDTO();

        trainerDTO.setFName("John");
        trainerDTO.setLName("Doe");
        trainerDTO.setSpecialization(1L);
        trainerDTO.setActive(true);
        trainerDTO.setTrainees(new ArrayList<>());

        when(trainerService.getTrainer(username))
                .thenReturn(trainerDTO);
    }

    @When("I request trainer {string}")
    public void requestTrainer(String username)
            throws Exception {

        var result = mockMvc.perform(
                get("/trainers")
                        .param("username", username)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    @Given("trainer {string} does not exist")
    public void trainerDoesNotExist(String username) {

        when(trainerService.getTrainer(username))
                .thenThrow(
                        new NotFoundException(
                                "Trainer not found"
                        )
                );
    }

    @When("I register trainer with empty first name")
    public void registerTrainerWithEmptyFirstName()
            throws Exception {

        String json = """
                {
                  "firstName": "",
                  "lastName": "Doe",
                  "specializationId": 1
                }
                """;

        var result = mockMvc.perform(
                post("/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    // ============================================================
    // TRAINEE
    // ============================================================

    @Given("valid trainee registration data")
    public void validTraineeRegistrationData() {

        RegistrationResponse registrationResponse =
                new RegistrationResponse();

        registrationResponse.setUsername("john.trainee");
        registrationResponse.setPassword("password123");

        when(traineeService.createTrainee(
                any(TraineeRegistrationRequest.class)
        )).thenReturn(registrationResponse);
    }

    @When("I register trainee with first name {string} and last name {string}")
    public void registerTrainee(
            String firstName,
            String lastName
    ) throws Exception {

        String json = """
                {
                  "firstName": "%s",
                  "lastName": "%s"
                }
                """.formatted(
                firstName,
                lastName
        );

        var result = mockMvc.perform(
                post("/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    @Given("trainee {string} exists")
    public void traineeExists(String username) {

        TraineeDTO traineeDTO = new TraineeDTO();

        traineeDTO.setFName("John");
        traineeDTO.setLName("Trainee");
        traineeDTO.setActive(true);
        traineeDTO.setTrainers(new ArrayList<>());

        when(traineeService.getTrainee(username))
                .thenReturn(traineeDTO);
    }

    @When("I request trainee {string}")
    public void requestTrainee(String username)
            throws Exception {

        var result = mockMvc.perform(
                get("/trainees")
                        .param("username", username)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    @Given("trainee {string} does not exist")
    public void traineeDoesNotExist(String username) {

        when(traineeService.getTrainee(username))
                .thenThrow(
                        new NotFoundException(
                                "Trainee not found"
                        )
                );
    }

    // ============================================================
    // TRAINING
    // ============================================================

    @Given("valid training data")
    public void validTrainingData() {

        when(trainingService.addTraining(
                any(AddTrainingRequest.class)
        )).thenReturn(
                org.springframework.http.ResponseEntity.ok().build()
        );
    }

    @When("I add a training")
    public void addTraining()
            throws Exception {

        String json = """
                {
                  "traineeUsername": "john.trainee",
                  "trainerUsername": "john.trainer",
                  "trainingName": "Java training",
                  "trainingDate": "2026-08-15T10:00:00.000+00:00",
                  "trainingDuration": 60
                }
                """;

        var result = mockMvc.perform(
                post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    @When("I delete training with id {long}")
    public void deleteTraining(long id)
            throws Exception {

        when(trainingService.deleteTraining(id))
                .thenReturn(
                        org.springframework.http.ResponseEntity
                                .ok()
                                .build()
                );

        var result = mockMvc.perform(
                delete("/trainings/{id}", id)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    @When("I add a training with empty trainee username")
    public void addTrainingWithEmptyTraineeUsername()
            throws Exception {

        String json = """
                {
                  "traineeUsername": "",
                  "trainerUsername": "john.trainer",
                  "trainingName": "Java training",
                  "trainingDate": "2026-08-15T10:00:00.000+00:00",
                  "trainingDuration": 60
                }
                """;

        var result = mockMvc.perform(
                post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    // ============================================================
    // TRAINING TYPE
    // ============================================================

    @Given("training types exist")
    public void trainingTypesExist() {

        TrainingType type = new TrainingType();

        when(trainingTypeService.getAllTrainingTypes())
                .thenReturn(
                        List.of(type)
                );
    }

    @When("I request all training types")
    public void requestAllTrainingTypes()
            throws Exception {

        var result = mockMvc.perform(
                get("/training-types")
        ).andReturn();

        responseStatus = result.getResponse().getStatus();
        response = result.getResponse().getContentAsString();
    }

    @And("the response should be a JSON array")
    public void responseShouldBeJsonArray() {

        org.junit.jupiter.api.Assertions.assertTrue(
                response.startsWith("[")
        );
    }

    // ============================================================
    // GENERIC
    // ============================================================

    @And("the response should contain {string}")
    public void responseShouldContain(String text) {

        org.junit.jupiter.api.Assertions.assertTrue(
                response.contains(text),
                "Response does not contain: " + text +
                        "\nActual response: " + response
        );
    }
}