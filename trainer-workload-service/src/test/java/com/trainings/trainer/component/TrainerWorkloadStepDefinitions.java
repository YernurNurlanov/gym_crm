package com.trainings.trainer.component;

import com.trainings.trainer.controller.TrainerWorkloadConsumer;
import com.trainings.trainer.dto.MonthSummary;
import com.trainings.trainer.dto.YearSummary;
import com.trainings.trainer.dto.request.TrainerWorkloadRequest;
import com.trainings.trainer.entity.ActionType;
import com.trainings.trainer.entity.TrainerWorkload;
import com.trainings.trainer.repository.TrainerWorkloadRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class TrainerWorkloadStepDefinitions {

    @Autowired
    private TrainerWorkloadConsumer consumer;

    @Autowired
    private TrainerWorkloadRepository repository;

    private TrainerWorkloadRequest request;
    private TrainerWorkload savedTrainer;
    private Exception thrownException;

    @Before
    public void setUp() {
        reset(repository);

        request = null;
        savedTrainer = null;
        thrownException = null;
    }

    @Given("a new trainer workload request:")
    public void aNewTrainerWorkloadRequest(DataTable dataTable) throws ParseException {

        Map<String, String> data = dataTable.asMap();

        request = new TrainerWorkloadRequest();

        request.setUsername(data.get("username"));
        request.setFirstName(data.get("firstName"));
        request.setLastName(data.get("lastName"));
        request.setActive(
                Boolean.parseBoolean(data.get("active"))
        );

        Date date = new SimpleDateFormat("yyyy-MM-dd")
                .parse(data.get("date"));

        request.setTrainingDate(date);

        request.setTrainingDuration(
                Integer.parseInt(data.get("duration"))
        );

        request.setActionType(
                ActionType.valueOf(data.get("action"))
        );
    }

    @Given("the trainer does not exist")
    public void theTrainerDoesNotExist() {

        when(repository.findByUsername(request.getUsername()))
                .thenReturn(Optional.empty());
    }

    @Given("an existing trainer {string} with workload {int} minutes for AUGUST 2026")
    public void anExistingTrainerWithWorkload(String username, int duration) {

        TrainerWorkload trainer = new TrainerWorkload();

        trainer.setUsername(username);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setActive(true);

        YearSummary yearSummary = new YearSummary();
        yearSummary.setCalendarYear(2026);

        MonthSummary monthSummary = new MonthSummary();
        monthSummary.setWorkloadMonth(Month.AUGUST);
        monthSummary.setDuration(duration);

        yearSummary.getMonths().add(monthSummary);
        trainer.getYears().add(yearSummary);

        when(repository.findByUsername(username))
                .thenReturn(Optional.of(trainer));
    }

    @When("the trainer workload message is received")
    public void theTrainerWorkloadMessageIsReceived() {

        try {
            consumer.receive(request);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the trainer workload is saved")
    public void theTrainerWorkloadIsSaved() {

        ArgumentCaptor<TrainerWorkload> captor =
                ArgumentCaptor.forClass(TrainerWorkload.class);

        verify(repository).save(captor.capture());

        savedTrainer = captor.getValue();
    }

    @Then("the trainer workload is not saved")
    public void theTrainerWorkloadIsNotSaved() {

        verify(repository, never())
                .save(any(TrainerWorkload.class));
    }

    @Then("the saved trainer has username {string}")
    public void theSavedTrainerHasUsername(String username) {

        assertNotNull(savedTrainer);

        assertEquals(
                username,
                savedTrainer.getUsername()
        );
    }

    @Then("the saved trainer has first name {string}")
    public void theSavedTrainerHasFirstName(String firstName) {

        assertNotNull(savedTrainer);

        assertEquals(
                firstName,
                savedTrainer.getFirstName()
        );
    }

    @Then("the saved trainer has last name {string}")
    public void theSavedTrainerHasLastName(String lastName) {

        assertNotNull(savedTrainer);

        assertEquals(
                lastName,
                savedTrainer.getLastName()
        );
    }

    @Then("the saved trainer is active")
    public void theSavedTrainerIsActive() {

        assertNotNull(savedTrainer);

        assertTrue(savedTrainer.isActive());
    }

    @Then("the saved workload for year {int} and month {word} is {int} minutes")
    public void theSavedWorkloadIs(
            int year,
            String month,
            int expectedDuration) {

        assertNotNull(savedTrainer);

        YearSummary yearSummary =
                savedTrainer.getYears()
                        .stream()
                        .filter(yearItem ->
                                yearItem.getCalendarYear() == year)
                        .findFirst()
                        .orElseThrow();

        MonthSummary monthSummary =
                yearSummary.getMonths()
                        .stream()
                        .filter(monthItem ->
                                monthItem.getWorkloadMonth()
                                        == Month.valueOf(month))
                        .findFirst()
                        .orElseThrow();

        assertEquals(
                expectedDuration,
                monthSummary.getDuration()
        );
    }

    @Then("the workload update fails with message {string}")
    public void theWorkloadUpdateFails(String message) {

        assertNotNull(thrownException);

        assertEquals(
                message,
                thrownException.getMessage()
        );
    }
}