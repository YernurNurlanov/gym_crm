package com.crm.gym.integration;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.repository.TrainingRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class IntegrationStepDefinitions {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @LocalServerPort
    private int port;

    private RestClient restClient;

    private Long createdTrainingId;

    private ResponseEntity<Void> lastResponse;

    private Exception lastException;

    private String token;

    private final MongoWorkloadTestRepository workloadRepository =
            new MongoWorkloadTestRepository();

    @Before
    public void setUp() {

        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        createdTrainingId = null;
        lastResponse = null;
        lastException = null;
        token = null;
    }

    @After
    public void tearDown() {

        if (createdTrainingId != null) {

            trainingRepository.findById(createdTrainingId)
                    .ifPresent(trainingRepository::delete);
        }

        workloadRepository.deleteAll();
    }

    @Given("the trainer workload database is clean")
    public void trainerWorkloadDatabaseIsClean() {

        workloadRepository.deleteAll();
    }

    @And("a trainee with username {string} exists")
    public void traineeExists(String username) {

        Optional<Trainee> existing =
                traineeRepository.findByUsername(username);

        if (existing.isPresent()) {
            return;
        }

        Trainee trainee = new Trainee();

        trainee.setUsername(username);
        trainee.setFirstName("string");
        trainee.setLastName("string5");
        trainee.setActive(true);

        traineeRepository.save(trainee);
    }

    @And("a trainer with username {string} exists")
    public void trainerExists(String username) {

        Optional<Trainer> existing =
                trainerRepository.findByUsername(username);

        if (existing.isPresent()) {
            return;
        }

        Trainer trainer = new Trainer();

        trainer.setUsername(username);
        trainer.setFirstName("string");
        trainer.setLastName("int");
        trainer.setActive(true);

        trainerRepository.save(trainer);
    }

    @And("no trainer with username {string} exists")
    public void trainerDoesNotExist(String username) {

        trainerRepository.findByUsername(username)
                .ifPresent(trainerRepository::delete);
    }

    private void authenticate() {

        try {

            Map<String, String> loginRequest = Map.of(
                    "username", "string.int",
                    "password", "UfUhDsLgng"
            );

            LoginResponse loginResponse = restClient.post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginRequest)
                    .retrieve()
                    .body(LoginResponse.class);

            Assertions.assertNotNull(
                    loginResponse,
                    "Login response is null"
            );

            Assertions.assertNotNull(
                    loginResponse.getToken(),
                    "Authentication token is null"
            );

            token = loginResponse.getToken();

        } catch (Exception e) {

            throw new AssertionError(
                    "Authentication failed: " + e.getMessage(),
                    e
            );
        }
    }

    @When(
            "a training for trainer {string} with duration {int} minutes is created"
    )
    public void createTraining(
            String trainerUsername,
            int duration) {

        try {

            if (token == null) {
                authenticate();
            }

            Map<String, Object> request = Map.of(
                    "traineeUsername", "string.string5",
                    "trainerUsername", trainerUsername,
                    "trainingName", "Integration Training",
                    "trainingDate", new Date(),
                    "trainingDuration", duration
            );

            lastResponse = restClient.post()
                    .uri("/trainings")
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + token
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            Optional<Training> createdTraining =
                    trainingRepository
                            .findAllByTrainer_UsernameOrderByIdDesc(trainerUsername)
                            .stream()
                            .findFirst();

            createdTrainingId = createdTraining
                    .map(Training::getId)
                    .orElse(null);

        } catch (Exception e) {

            lastException = e;
        }
    }

    @Then("the training creation request is successful")
    public void trainingCreationIsSuccessful() {

        Assertions.assertNull(
                lastException,
                () -> "Training creation failed: " + lastException
        );

        Assertions.assertNotNull(lastResponse);

        Assertions.assertEquals(
                HttpStatus.OK,
                lastResponse.getStatusCode()
        );

        Assertions.assertNotNull(
                createdTrainingId,
                "Training was not created"
        );
    }

    @Then("the training creation request fails")
    public void trainingCreationFails() {

        Assertions.assertNotNull(
                lastException,
                "Training creation should have failed"
        );
    }

    @And("the workload is processed")
    public void workloadIsProcessed() {

        await()
                .atMost(10, TimeUnit.SECONDS)
                .until(() ->
                        !workloadRepository.findAll().isEmpty());
    }

    @Then(
            "trainer workload for {string} contains {int} minutes"
    )
    public void trainerWorkloadContains(
            String username,
            int expectedDuration) {

        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {

                    Map<String, Object> workload =
                            workloadRepository
                                    .findByUsername(username)
                                    .orElseThrow(() ->
                                            new AssertionError(
                                                    "Trainer workload not found for "
                                                            + username
                                            )
                                    );

                    int actualDuration =
                            workloadRepository
                                    .calculateTotalDuration(workload);

                    Assertions.assertEquals(
                            expectedDuration,
                            actualDuration
                    );
                });
    }

    @Then(
            "trainer workload for {string} does not exist"
    )
    public void trainerWorkloadDoesNotExist(String username) {

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        Assertions.assertTrue(
                                workloadRepository
                                        .findByUsername(username)
                                        .isEmpty()
                        )
                );
    }

    @When("the created training is deleted")
    public void deleteCreatedTraining() {

        Assertions.assertNotNull(
                createdTrainingId,
                "Training was not created"
        );

        try {

            lastResponse = restClient.delete()
                    .uri("/trainings/{id}", createdTrainingId)
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + token
                    )
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {

            lastException = e;
        }
    }

    @Then("the training deletion request is successful")
    public void trainingDeletionIsSuccessful() {

        Assertions.assertNull(
                lastException,
                () -> "Training deletion failed: " + lastException
        );

        Assertions.assertNotNull(lastResponse);

        Assertions.assertEquals(
                HttpStatus.OK,
                lastResponse.getStatusCode()
        );
    }

    private record LoginResponse(
            String token,
            String username
    ) {
        public String getToken() {
            return token;
        }

        public String getUsername() {
            return username;
        }
    }
}