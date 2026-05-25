package com.crm.gym.service;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.util.CredentialGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    private TraineeRepository traineeRepository;

    private TrainerRepository trainerRepository;

    private CredentialGenerator credentialGenerator;

    private TraineeService traineeService;

    @BeforeEach
    void setUp() {

        traineeRepository = mock(TraineeRepository.class);

        trainerRepository = mock(TrainerRepository.class);

        credentialGenerator = mock(CredentialGenerator.class);

        traineeService = new TraineeService(
                traineeRepository,
                trainerRepository,
                credentialGenerator
        );
    }

    @Test
    void shouldCreateTrainee() {

        Trainee trainee = new Trainee();

        trainee.setFirstName("John");

        trainee.setLastName("Doe");

        Trainee savedTrainee = new Trainee();

        savedTrainee.setUserId(1L);

        when(credentialGenerator.generateUniqueUsername("John", "Doe"))
                .thenReturn("John.Doe");

        when(credentialGenerator.generatePassword())
                .thenReturn("pswd123");

        when(traineeRepository.save(trainee))
                .thenReturn(savedTrainee);

        Trainee result =
                traineeService.createTrainee(trainee);

        assertNotNull(result);

        assertEquals(1L, result.getUserId());

        assertEquals("John.Doe", trainee.getUsername());

        assertEquals("pswd123", trainee.getPassword());
    }

    @Test
    void shouldUpdateTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeRepository.findById(1L))
                .thenReturn(Optional.of(trainee));

        traineeService.updateTrainee(trainee);

        verify(traineeRepository, times(1))
                .save(trainee);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> traineeService.updateTrainee(trainee));

        assertEquals(
                "Trainer not found",
                exception.getMessage());

        verify(traineeRepository, never())
                .save(any());
    }

    @Test
    void shouldChangeTraineePassword() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeRepository.findById(1L))
                .thenReturn(Optional.of(trainee));

        when(traineeRepository.save(trainee))
                .thenReturn(trainee);

        Trainee result =
                traineeService.changeTraineePassword(1L, "newPassword");

        assertNotNull(result);

        assertEquals("newPassword", trainee.getPassword());
    }

    @Test
    void shouldToggleTraineeStatus() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        trainee.setActive(false);

        when(traineeRepository.findById(1L))
                .thenReturn(Optional.of(trainee));

        traineeService.toggleTraineeStatus(1L);

        assertTrue(trainee.isActive());

        verify(traineeRepository, times(1))
                .save(trainee);
    }

    @Test
    void shouldSelectTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeRepository.findById(1L))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> result =
                traineeService.selectTrainee(1L);

        assertTrue(result.isPresent());

        assertEquals(trainee, result.get());
    }

    @Test
    void shouldDeleteTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUsername("Alex.Brown");

        when(traineeRepository.findByUsername("Alex.Brown"))
                .thenReturn(Optional.of(trainee));

        traineeService.deleteTrainee("Alex.Brown");

        verify(traineeRepository, times(1))
                .delete(trainee);
    }

    @Test
    void shouldGetTraineeTrainings() {

        Trainee trainee = new Trainee();

        Trainer trainer = new Trainer();

        Date from = new Date();

        Date to = new Date();

        TrainingType type = new TrainingType();

        List<Training> trainings =
                List.of(new Training());

        when(traineeRepository.findByUsername("alex"))
                .thenReturn(Optional.of(trainee));

        when(trainerRepository.findByUsername("ivan"))
                .thenReturn(Optional.of(trainer));

        when(traineeRepository.getTrainings(trainee, from, to, "alex", type))
                .thenReturn(trainings);

        List<Training> result =
                traineeService.getTraineeTrainings("alex", from, to, "ivan", type);

        assertNotNull(result);

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowExceptionWhenGetTrainingsWithMissingTrainee() {

        when(traineeRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> traineeService.getTraineeTrainings("missing", null, null, "ivan", null));

        assertEquals(
                "Trainee not found",
                exception.getMessage());
    }

    @Test
    void shouldUpdateTraineeTrainers() {

        Trainee trainee = new Trainee();

        trainee.setUsername("alex");

        trainee.setTrainers(new ArrayList<>());

        List<String> trainerUsernames =
                List.of("ivan", "petr");

        List<Trainer> newTrainers =
                List.of(new Trainer(), new Trainer());

        when(traineeRepository.findByUsername("alex"))
                .thenReturn(Optional.of(trainee));

        when(trainerRepository.findByUsernames(trainerUsernames))
                .thenReturn(newTrainers);

        when(traineeRepository.save(trainee))
                .thenReturn(trainee);

        List<Trainer> result =
                traineeService.updateTraineeTrainers("alex", trainerUsernames);

        assertNotNull(result);

        assertEquals(2, result.size());

        verify(traineeRepository, times(1))
                .save(trainee);
    }

    @Test
    void shouldThrowExceptionWhenUpdateTrainersWithMissingTrainee() {

        List<String> usernames =
                List.of("ivan");

        when(traineeRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> traineeService.updateTraineeTrainers("missing", usernames));

        assertEquals(
                "Trainee not found: missing",
                exception.getMessage());
    }
}