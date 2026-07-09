//package com.crm.gym.service;
//
//import com.crm.gym.entity.Trainee;
//import com.crm.gym.entity.Trainer;
//import com.crm.gym.entity.Training;
//import com.crm.gym.entity.TrainingType;
//import com.crm.gym.repository.TraineeRepository;
//import com.crm.gym.repository.TrainerRepository;
//import com.crm.gym.repository.TrainingRepository;
//import com.crm.gym.util.CredentialGenerator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TraineeServiceTest {
//
//    private TraineeRepository traineeRepository;
//    private TrainerRepository trainerRepository;
//    private TrainingRepository trainingRepository;
//    private CredentialGenerator credentialGenerator;
//
//    private TraineeService traineeService;
//
//    @BeforeEach
//    void setUp() {
//        traineeRepository = mock(TraineeRepository.class);
//        trainerRepository = mock(TrainerRepository.class);
//        trainingRepository = mock(TrainingRepository.class);
//        credentialGenerator = mock(CredentialGenerator.class);
//
//        traineeService = new TraineeService(
//                traineeRepository,
//                trainerRepository,
//                credentialGenerator,
//                trainingRepository
//        );
//    }
//
//    @Test
//    void shouldCreateTrainee() {
//
//        Trainee trainee = new Trainee();
//        trainee.setFirstName("John");
//        trainee.setLastName("Doe");
//
//        Trainee saved = new Trainee();
//        saved.setUserId(1L);
//
//        when(credentialGenerator.generateUniqueUsername("John", "Doe"))
//                .thenReturn("John.Doe");
//
//        when(credentialGenerator.generatePassword())
//                .thenReturn("password123");
//
//        when(traineeRepository.save(any(Trainee.class)))
//                .thenReturn(saved);
//
//        Trainee result = traineeService.createTrainee(trainee);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getUserId());
//        assertEquals("John.Doe", trainee.getUsername());
//        assertEquals("password123", trainee.getPassword());
//
//        verify(traineeRepository).save(trainee);
//    }
//
//    @Test
//    void shouldUpdateTrainee() {
//
//        Trainee trainee = new Trainee();
//        trainee.setUserId(1L);
//
//        when(traineeRepository.findById(1L))
//                .thenReturn(Optional.of(trainee));
//
//        traineeService.updateTrainee(trainee);
//
//        verify(traineeRepository).save(trainee);
//    }
//
//    @Test
//    void shouldThrowWhenUpdatingMissingTrainee() {
//
//        Trainee trainee = new Trainee();
//        trainee.setUserId(1L);
//
//        when(traineeRepository.findById(1L))
//                .thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(
//                RuntimeException.class,
//                () -> traineeService.updateTrainee(trainee)
//        );
//
//        assertEquals("Trainee not found", ex.getMessage());
//
//        verify(traineeRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldChangePassword() {
//
//        Trainee trainee = new Trainee();
//        trainee.setUserId(1L);
//
//        when(traineeRepository.findById(1L))
//                .thenReturn(Optional.of(trainee));
//
//        when(traineeRepository.save(any(Trainee.class)))
//                .thenReturn(trainee);
//
//        Trainee result = traineeService.changeTraineePassword(1L, "newPassword");
//
//        assertEquals("newPassword", trainee.getPassword());
//        assertNotNull(result);
//    }
//
//    @Test
//    void shouldToggleStatus() {
//
//        Trainee trainee = new Trainee();
//        trainee.setUserId(1L);
//        trainee.setActive(false);
//
//        when(traineeRepository.findById(1L))
//                .thenReturn(Optional.of(trainee));
//
//        traineeService.toggleTraineeStatus(1L);
//
//        assertTrue(trainee.isActive());
//
//        verify(traineeRepository).save(trainee);
//    }
//
//    @Test
//    void shouldSelectById() {
//
//        Trainee trainee = new Trainee();
//        trainee.setUserId(1L);
//
//        when(traineeRepository.findById(1L))
//                .thenReturn(Optional.of(trainee));
//
//        Optional<Trainee> result = traineeService.selectTrainee(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(trainee, result.get());
//    }
//
//    @Test
//    void shouldDeleteTrainee() {
//
//        Trainee trainee = new Trainee();
//        trainee.setUsername("Alex.Brown");
//
//        when(traineeRepository.findByUsername("Alex.Brown"))
//                .thenReturn(Optional.of(trainee));
//
//        traineeService.deleteTrainee("Alex.Brown");
//
//        verify(traineeRepository).delete(trainee);
//    }
//
//    @Test
//    void shouldGetTrainings() {
//
//        Trainee trainee = new Trainee();
//        Trainer trainer = new Trainer();
//        TrainingType type = new TrainingType();
//
//        Date from = new Date();
//        Date to = new Date();
//
//        List<Training> trainings = List.of(new Training());
//
//        when(traineeRepository.findByUsername("alex"))
//                .thenReturn(Optional.of(trainee));
//
//        when(trainerRepository.findByUsername("ivan"))
//                .thenReturn(Optional.of(trainer));
//
//        when(trainingRepository.findTraineeTrainings(
//                eq("alex"),
//                eq(from),
//                eq(to),
//                eq("ivan"),
//                eq(type)
//        )).thenReturn(trainings);
//
//        List<Training> result =
//                traineeService.getTraineeTrainings("alex", from, to, "ivan", type);
//
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void shouldThrowWhenTraineeMissingForTrainings() {
//
//        when(traineeRepository.findByUsername("missing"))
//                .thenReturn(Optional.empty());
//
//        assertThrows(
//                RuntimeException.class,
//                () -> traineeService.getTraineeTrainings(
//                        "missing",
//                        null,
//                        null,
//                        "ivan",
//                        null
//                )
//        );
//    }
//
//    @Test
//    void shouldUpdateTraineeTrainers() {
//
//        Trainee trainee = new Trainee();
//        trainee.setUsername("alex");
//        trainee.setTrainers(new ArrayList<>());
//
//        List<String> usernames = List.of("ivan", "petr");
//        List<Trainer> trainers = List.of(new Trainer(), new Trainer());
//
//        when(traineeRepository.findByUsername("alex"))
//                .thenReturn(Optional.of(trainee));
//
//        when(trainerRepository.findByUsernameIn(usernames))
//                .thenReturn(trainers);
//
//        when(traineeRepository.save(any(Trainee.class)))
//                .thenReturn(trainee);
//
//        List<Trainer> result =
//                traineeService.updateTraineeTrainers("alex", usernames);
//
//        assertEquals(2, result.size());
//
//        verify(traineeRepository).save(trainee);
//    }
//
//    @Test
//    void shouldThrowWhenUpdatingTrainersAndTraineeMissing() {
//
//        List<String> usernames = List.of("ivan");
//
//        when(traineeRepository.findByUsername("missing"))
//                .thenReturn(Optional.empty());
//
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> traineeService.updateTraineeTrainers("missing", usernames)
//        );
//    }
//}