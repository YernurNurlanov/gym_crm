package com.crm.gym;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import com.crm.gym.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GymFacadeTest {

    private TrainerService trainerService;

    private TraineeService traineeService;

    private TrainingService trainingService;

    private TrainingTypeService trainingTypeService;

    private AuthenticationService authenticationService;

    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {

        trainerService = mock(TrainerService.class);

        traineeService = mock(TraineeService.class);

        trainingService = mock(TrainingService.class);

        trainingTypeService = mock(TrainingTypeService.class);

        authenticationService = mock(AuthenticationService.class);

        gymFacade = new GymFacade(
                trainerService,
                traineeService,
                trainingService,
                trainingTypeService,
                authenticationService
        );
    }

    // Authentication operations tests

    @Test
    void shouldDelegateAuthenticate() {

        when(authenticationService.authenticate("user", "pass"))
                .thenReturn(true);

        boolean result =
                gymFacade.authenticate("user", "pass");

        assertTrue(result);

        verify(authenticationService, times(1))
                .authenticate("user", "pass");
    }

    @Test
    void shouldDelegateLogout() {

        gymFacade.logout();

        verify(authenticationService, times(1))
                .logout();
    }

    // Trainer operations tests

    @Test
    void shouldDelegateCreateTrainer() {

        Trainer trainer = new Trainer();

        when(trainerService.createTrainer(trainer))
                .thenReturn(trainer);

        Trainer result =
                gymFacade.createTrainer(trainer);

        assertEquals(trainer, result);

        verify(trainerService, times(1))
                .createTrainer(trainer);
    }

    @Test
    void shouldDelegateSelectTrainer() {

        Trainer trainer = new Trainer();

        when(trainerService.selectTrainer(1L))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result =
                gymFacade.selectTrainer(1L);

        assertTrue(result.isPresent());

        assertEquals(trainer, result.get());
    }

    @Test
    void shouldDelegateSelectTrainerByUsername() {

        Trainer trainer = new Trainer();

        when(trainerService.selectTrainerByUsername("ivan"))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result =
                gymFacade.selectTrainerByUsername("ivan");

        assertTrue(result.isPresent());

        assertEquals(trainer, result.get());
    }

    @Test
    void shouldDelegateToggleTrainerStatus() {

        gymFacade.toggleTrainerStatus(1L);

        verify(trainerService, times(1))
                .toggleTrainerStatus(1L);
    }

    @Test
    void shouldDelegateChangeTrainerPassword() {

        Trainer trainer = new Trainer();

        when(trainerService.changeTrainerPassword(1L, "newPass"))
                .thenReturn(trainer);

        Trainer result =
                gymFacade.changeTrainerPassword(1L, "newPass");

        assertEquals(trainer, result);
    }

    @Test
    void shouldDelegateGetTrainerTrainings() {

        Date from = new Date();

        Date to = new Date();

        List<Training> trainings =
                List.of(new Training());

        when(trainerService.getTrainerTrainings("ivan", from, to, "alex"))
                .thenReturn(trainings);

        List<Training> result =
                gymFacade.getTrainerTrainings("ivan", from, to, "alex");

        assertEquals(1, result.size());
    }

    @Test
    void shouldDelegateSelectAllTrainers() {

        List<Trainer> trainers =
                List.of(new Trainer(), new Trainer());

        when(trainerService.selectAllTrainers())
                .thenReturn(trainers);

        List<Trainer> result =
                gymFacade.selectAllTrainers();

        assertEquals(2, result.size());
    }

    @Test
    void shouldDelegateUpdateTrainer() {

        Trainer trainer = new Trainer();

        gymFacade.updateTrainer(trainer);

        verify(trainerService, times(1))
                .updateTrainer(trainer);
    }

    @Test
    void shouldDelegateGetTrainerNotAssignedToTrainee() {

        List<Trainer> trainers =
                List.of(new Trainer());

        when(trainerService.getTrainersNotAssignedToTrainee("alex"))
                .thenReturn(trainers);

        List<Trainer> result =
                gymFacade.getTrainerNotAssignedToTrainee("alex");

        assertEquals(1, result.size());
    }

    // Trainee operations tests

    @Test
    void shouldDelegateCreateTrainee() {

        Trainee trainee = new Trainee();

        when(traineeService.createTrainee(trainee))
                .thenReturn(trainee);

        Trainee result =
                gymFacade.createTrainee(trainee);

        assertEquals(trainee, result);
    }

    @Test
    void shouldDelegateSelectTrainee() {

        Trainee trainee = new Trainee();

        when(traineeService.selectTrainee(1L))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> result =
                gymFacade.selectTrainee(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldDelegateSelectTraineeByUsername() {

        Trainee trainee = new Trainee();

        when(traineeService.selectTraineeByUsername("alex"))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> result =
                gymFacade.selectTraineeByUsername("alex");

        assertTrue(result.isPresent());
    }

    @Test
    void shouldDelegateSelectAllTrainees() {

        List<Trainee> trainees =
                List.of(new Trainee());

        when(traineeService.selectAllTrainees())
                .thenReturn(trainees);

        List<Trainee> result =
                gymFacade.selectAllTrainees();

        assertEquals(1, result.size());
    }

    @Test
    void shouldDelegateUpdateTrainee() {

        Trainee trainee = new Trainee();

        gymFacade.updateTrainee(trainee);

        verify(traineeService, times(1))
                .updateTrainee(trainee);
    }

    @Test
    void shouldDelegateChangeTraineePassword() {

        Trainee trainee = new Trainee();

        when(traineeService.changeTraineePassword(1L, "pass"))
                .thenReturn(trainee);

        Trainee result =
                gymFacade.changeTraineePassword(1L, "pass");

        assertEquals(trainee, result);
    }

    @Test
    void shouldDelegateToggleTraineeStatus() {

        gymFacade.toggleTraineeStatus(1L);

        verify(traineeService, times(1))
                .toggleTraineeStatus(1L);
    }

    @Test
    void shouldDelegateDeleteTrainee() {

        gymFacade.deleteTrainee("alex");

        verify(traineeService, times(1))
                .deleteTrainee("alex");
    }

    @Test
    void shouldDelegateGetTraineeTrainings() {

        Date from = new Date();

        Date to = new Date();

        TrainingType type = new TrainingType();

        List<Training> trainings =
                List.of(new Training());

        when(traineeService.getTraineeTrainings("alex", from, to, "ivan", type))
                .thenReturn(trainings);

        List<Training> result =
                gymFacade.getTraineeTrainings("alex", from, to, "ivan", type);

        assertEquals(1, result.size());
    }

    // Training operations tests

    @Test
    void shouldDelegateCreateTraining() {

        Training training = new Training();

        when(trainingService.createTraining(training))
                .thenReturn(training);

        Training result =
                gymFacade.createTraining(training);

        assertEquals(training, result);
    }

    // Training Type operations tests

    @Test
    void shouldDelegateGetTrainingTypeById() {

        TrainingType type = new TrainingType();

        when(trainingTypeService.getTrainingTypeById(1L))
                .thenReturn(Optional.of(type));

        Optional<TrainingType> result =
                gymFacade.getTrainingTypeById(1L);

        assertTrue(result.isPresent());
    }
}