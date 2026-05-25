package com.crm.gym.service;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.util.CredentialGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerRepository trainerRepository;

    private CredentialGenerator credentialGenerator;

    private TraineeRepository traineeRepository;

    private TrainerService trainerService;

    @BeforeEach
    void setUp() {

        trainerRepository = mock(TrainerRepository.class);

        credentialGenerator = mock(CredentialGenerator.class);

        traineeRepository = mock(TraineeRepository.class);

        trainerService = new TrainerService(
                trainerRepository,
                credentialGenerator,
                traineeRepository
        );
    }

    @Test
    void shouldCreateTrainer() {

        Trainer trainer = new Trainer();

        trainer.setFirstName("John");

        trainer.setLastName("Smith");

        Trainer savedTrainer = new Trainer();

        savedTrainer.setUserId(1L);

        when(credentialGenerator.generateUniqueUsername("John", "Smith"))
                .thenReturn("John.Smith");

        when(credentialGenerator.generatePassword())
                .thenReturn("secure123");

        when(trainerRepository.save(trainer))
                .thenReturn(savedTrainer);

        Trainer result =
                trainerService.createTrainer(trainer);

        assertNotNull(result);

        assertEquals(1L, result.getUserId());

        assertEquals("John.Smith", trainer.getUsername());

        assertEquals("secure123", trainer.getPassword());
    }

    @Test
    void shouldUpdateTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        trainerService.updateTrainer(trainer);

        verify(trainerRepository, times(1))
                .save(trainer);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> trainerService.updateTrainer(trainer));

        assertEquals(
                "Trainer not found",
                exception.getMessage());

        verify(trainerRepository, never())
                .save(any());
    }

    @Test
    void shouldToggleTrainerStatus() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        trainer.setActive(true);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        trainerService.toggleTrainerStatus(1L);

        assertFalse(trainer.isActive());

        verify(trainerRepository, times(1))
                .save(trainer);
    }

    @Test
    void shouldThrowExceptionWhenTogglingStatusOnMissingTrainer() {

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> trainerService.toggleTrainerStatus(1L));

        assertEquals(
                "Trainer not found",
                exception.getMessage());
    }

    @Test
    void shouldChangeTrainerPassword() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        when(trainerRepository.save(trainer))
                .thenReturn(trainer);

        Trainer result =
                trainerService.changeTrainerPassword(1L, "newPassword");

        assertNotNull(result);

        assertEquals("newPassword", trainer.getPassword());
    }

    @Test
    void shouldGetTrainerTrainings() {

        Trainer trainer = new Trainer();

        Trainee trainee = new Trainee();

        Date from = new Date();

        Date to = new Date();

        List<Training> trainings =
                List.of(new Training());

        when(trainerRepository.findByUsername("coach.ivan"))
                .thenReturn(Optional.of(trainer));

        when(traineeRepository.findByUsername("alex.brown"))
                .thenReturn(Optional.of(trainee));

        when(trainerRepository.getTrainings(trainer, from, to, "alex.brown"))
                .thenReturn(trainings);

        List<Training> result =
                trainerService.getTrainerTrainings("coach.ivan", from, to, "alex.brown");

        assertNotNull(result);

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowExceptionWhenGetTrainingsWithMissingTrainer() {

        when(trainerRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> trainerService.getTrainerTrainings("missing", null, null, "alex"));

        assertEquals(
                "Trainer not found",
                exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGetTrainingsWithMissingTrainee() {

        Trainer trainer = new Trainer();

        when(trainerRepository.findByUsername("coach.ivan"))
                .thenReturn(Optional.of(trainer));

        when(traineeRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> trainerService.getTrainerTrainings("coach.ivan", null, null, "missing"));

        assertEquals(
                "Trainee not found",
                exception.getMessage());
    }

    @Test
    void shouldSelectTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result =
                trainerService.selectTrainer(1L);

        assertTrue(result.isPresent());

        assertEquals(trainer, result.get());
    }

    @Test
    void shouldSelectTrainerByUsername() {

        Trainer trainer = new Trainer();

        trainer.setUsername("coach.ivan");

        when(trainerRepository.findByUsername("coach.ivan"))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result =
                trainerService.selectTrainerByUsername("coach.ivan");

        assertTrue(result.isPresent());

        assertEquals(trainer, result.get());
    }

    @Test
    void shouldSelectAllTrainers() {

        List<Trainer> trainers =
                List.of(new Trainer(), new Trainer());

        when(trainerRepository.findAll())
                .thenReturn(trainers);

        List<Trainer> result =
                trainerService.selectAllTrainers();

        assertEquals(2, result.size());
    }

    @Test
    void shouldGetTrainersNotAssignedToTrainee() {

        List<Trainer> trainers =
                List.of(new Trainer());

        when(trainerRepository.getTrainersNotAssignedToTrainee("alex"))
                .thenReturn(trainers);

        List<Trainer> result =
                trainerService.getTrainersNotAssignedToTrainee("alex");

        assertEquals(1, result.size());
    }
}