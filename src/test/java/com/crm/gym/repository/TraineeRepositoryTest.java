package com.crm.gym.repository;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeRepositoryTest {

    private EntityManager em;

    private TypedQuery<Trainee> typedQuery;

    private TraineeRepository traineeRepository;

    @BeforeEach
    void setUp() {

        em = mock(EntityManager.class);

        typedQuery = mock(TypedQuery.class);

        traineeRepository = new TraineeRepository(em);
    }

    @Test
    void shouldPersistNewTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(null);

        Trainee result =
                traineeRepository.save(trainee);

        assertEquals(trainee, result);

        verify(em, times(1))
                .persist(trainee);

        verify(em, never())
                .merge(any());
    }

    @Test
    void shouldMergeExistingTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(em.merge(trainee))
                .thenReturn(trainee);

        Trainee result =
                traineeRepository.save(trainee);

        assertEquals(trainee, result);

        verify(em, times(1))
                .merge(trainee);

        verify(em, never())
                .persist(any());
    }

    @Test
    void shouldFindTraineeById() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(em.find(Trainee.class, 1L))
                .thenReturn(trainee);

        Optional<Trainee> result =
                traineeRepository.findById(1L);

        assertTrue(result.isPresent());

        assertEquals(trainee, result.get());
    }

    @Test
    void shouldReturnEmptyOptionalWhenTraineeNotFoundById() {

        when(em.find(Trainee.class, 1L))
                .thenReturn(null);

        Optional<Trainee> result =
                traineeRepository.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindTraineeByUsername() {

        Trainee trainee = new Trainee();

        trainee.setUsername("alex.brown");

        String jpql = "SELECT t\nFROM Trainee t\nWHERE t.username = :username\n";

        when(em.createQuery(jpql, Trainee.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter("username", "alex.brown"))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(List.of(trainee));

        Optional<Trainee> result =
                traineeRepository.findByUsername("alex.brown");

        assertTrue(result.isPresent());

        assertEquals(trainee, result.get());
    }

    @Test
    void shouldDeleteTrainee() {

        Trainee trainee = new Trainee();

        traineeRepository.delete(trainee);

        verify(em, times(1))
                .remove(trainee);
    }

    @Test
    void shouldReturnAllTrainees() {

        List<Trainee> trainees =
                List.of(new Trainee(), new Trainee());

        when(em.createQuery("SELECT t FROM Trainee t", Trainee.class))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(trainees);

        List<Trainee> result =
                traineeRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldGetTrainingsFilteredByTrainerTypeAndDates() {

        Trainee trainee = new Trainee();

        Trainer trainer = new Trainer();

        trainer.setUsername("coach.ivan");

        TrainingType type = new TrainingType();

        Calendar calendar = Calendar.getInstance();

        calendar.set(2026, Calendar.MAY, 15);
        Date trainingDate = calendar.getTime();

        calendar.set(2026, Calendar.MAY, 1);
        Date fromDate = calendar.getTime();

        calendar.set(2026, Calendar.MAY, 31);
        Date toDate = calendar.getTime();

        Training training = new Training();

        training.setTrainer(trainer);

        training.setTrainingType(type);

        training.setTrainingDate(trainingDate);

        List<Training> trainingList = new ArrayList<>();

        trainingList.add(training);

        trainee.setTrainings(trainingList);

        List<Training> result =
                traineeRepository.getTrainings(trainee, fromDate, toDate, "coach.ivan", type);

        assertEquals(1, result.size());
    }
}