package com.crm.gym.service;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.repository.TrainingRepository;
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
    private TrainingRepository trainingRepository;

    private TrainerService trainerService;

    @BeforeEach
    void setUp() {

        trainerRepository = mock(TrainerRepository.class);
        credentialGenerator = mock(CredentialGenerator.class);
        traineeRepository = mock(TraineeRepository.class);
        trainingRepository = mock(TrainingRepository.class);

        trainerService = new TrainerService(
                trainerRepository,
                credentialGenerator,
                traineeRepository,
                trainingRepository
        );
    }

    @Test
    void shouldCreateTrainer() {

        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Smith");

        Trainer saved = new Trainer();
        saved.setUserId(1L);

        when(credentialGenerator.generateUniqueUsername("John", "Smith"))
                .thenReturn("John.Smith");

        when(credentialGenerator.generatePassword())
                .thenReturn("secure123");

        when(trainerRepository.save(any(Trainer.class)))
                .thenReturn(saved);

        Trainer result = trainerService.createTrainer(trainer);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());

        assertEquals("John.Smith", trainer.getUsername());
        assertEquals("secure123", trainer.getPassword());

        verify(trainerRepository).save(trainer);
    }

    @Test
    void shouldUpdateTrainer() {

        Trainer trainer = new Trainer();
        trainer.setUserId(1L);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        trainerService.updateTrainer(trainer);

        verify(trainerRepository).save(trainer);
    }

    @Test
    void shouldThrowWhenUpdatingMissingTrainer() {

        Trainer trainer = new Trainer();
        trainer.setUserId(1L);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> trainerService.updateTrainer(trainer)
        );

        assertEquals("Trainer not found", ex.getMessage());

        verify(trainerRepository, never()).save(any());
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

        verify(trainerRepository).save(trainer);
    }

    @Test
    void shouldThrowWhenTogglingMissingTrainer() {

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> trainerService.toggleTrainerStatus(1L)
        );

        assertEquals("Trainer not found", ex.getMessage());
    }

    @Test
    void shouldChangePassword() {

        Trainer trainer = new Trainer();
        trainer.setUserId(1L);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        when(trainerRepository.save(any(Trainer.class)))
                .thenReturn(trainer);

        Trainer result = trainerService.changeTrainerPassword(1L, "newPassword");

        assertEquals("newPassword", trainer.getPassword());
        assertNotNull(result);

        verify(trainerRepository).save(trainer);
    }

    @Test
    void shouldGetTrainerTrainings() {

        Trainer trainer = new Trainer();
        trainer.setUsername("coach.ivan");

        Trainee trainee = new Trainee();

        Date from = new Date();
        Date to = new Date();

        List<Training> trainings = List.of(new Training());

        when(trainerRepository.findByUsername("coach.ivan"))
                .thenReturn(Optional.of(trainer));

        when(traineeRepository.findByUsername("alex.brown"))
                .thenReturn(Optional.of(trainee));

        when(trainingRepository.findTrainerTrainings(
                "coach.ivan",
                from,
                to,
                "alex.brown"
        )).thenReturn(trainings);

        List<Training> result =
                trainerService.getTrainerTrainings("coach.ivan", from, to, "alex.brown");

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowWhenTrainerMissing() {

        when(trainerRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> trainerService.getTrainerTrainings("missing", null, null, "alex")
        );
    }

    @Test
    void shouldThrowWhenTraineeMissing() {

        Trainer trainer = new Trainer();

        when(trainerRepository.findByUsername("coach.ivan"))
                .thenReturn(Optional.of(trainer));

        when(traineeRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> trainerService.getTrainerTrainings("coach.ivan", null, null, "missing")
        );
    }

    @Test
    void shouldSelectTrainer() {

        Trainer trainer = new Trainer();
        trainer.setUserId(1L);

        when(trainerRepository.findById(1L))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.selectTrainer(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldSelectByUsername() {

        Trainer trainer = new Trainer();
        trainer.setUsername("coach.ivan");

        when(trainerRepository.findByUsername("coach.ivan"))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result =
                trainerService.selectTrainerByUsername("coach.ivan");

        assertTrue(result.isPresent());
    }

    @Test
    void shouldSelectAll() {

        when(trainerRepository.findAll())
                .thenReturn(List.of(new Trainer(), new Trainer()));

        List<Trainer> result = trainerService.selectAllTrainers();

        assertEquals(2, result.size());
    }

    @Test
    void shouldGetNotAssignedTrainers() {

        when(trainerRepository.findActiveTrainersNotAssignedToTrainee("alex"))
                .thenReturn(List.of(new Trainer()));

        List<Trainer> result =
                trainerService.getTrainersNotAssignedToTrainee("alex");

        assertEquals(1, result.size());
    }
}