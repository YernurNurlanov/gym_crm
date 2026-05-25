package com.crm.gym.repository;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
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

class TrainerRepositoryTest {

    private EntityManager em;

    private TypedQuery<Trainer> typedQuery;

    private TrainerRepository trainerRepository;

    @BeforeEach
    void setUp() {

        em = mock(EntityManager.class);

        typedQuery = mock(TypedQuery.class);

        trainerRepository = new TrainerRepository(em);
    }

    @Test
    void shouldPersistNewTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(null);

        Trainer result =
                trainerRepository.save(trainer);

        assertEquals(trainer, result);

        verify(em, times(1))
                .persist(trainer);

        verify(em, never())
                .merge(any());
    }

    @Test
    void shouldMergeExistingTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(em.merge(trainer))
                .thenReturn(trainer);

        Trainer result =
                trainerRepository.save(trainer);

        assertEquals(trainer, result);

        verify(em, times(1))
                .merge(trainer);

        verify(em, never())
                .persist(any());
    }

    @Test
    void shouldFindTrainerById() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(em.find(Trainer.class, 1L))
                .thenReturn(trainer);

        Optional<Trainer> result =
                trainerRepository.findById(1L);

        assertTrue(result.isPresent());

        assertEquals(trainer, result.get());
    }

    @Test
    void shouldReturnEmptyOptionalWhenTrainerNotFoundById() {

        when(em.find(Trainer.class, 1L))
                .thenReturn(null);

        Optional<Trainer> result =
                trainerRepository.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindTrainerByUsername() {

        Trainer trainer = new Trainer();

        trainer.setUsername("coach.ivan");

        String jpql = "SELECT t\nFROM Trainer t\nWHERE t.username = :username\n";

        when(em.createQuery(jpql, Trainer.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter("username", "coach.ivan"))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(List.of(trainer));

        Optional<Trainer> result =
                trainerRepository.findByUsername("coach.ivan");

        assertTrue(result.isPresent());

        assertEquals(trainer, result.get());
    }

    @Test
    void shouldReturnAllTrainers() {

        List<Trainer> trainers =
                List.of(new Trainer(), new Trainer());

        when(em.createQuery("SELECT t FROM Trainer t", Trainer.class))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(trainers);

        List<Trainer> result =
                trainerRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldDeleteTrainer() {

        Trainer trainer = new Trainer();

        trainerRepository.delete(trainer);

        verify(em, times(1))
                .remove(trainer);
    }

    @Test
    void shouldGetTrainingsFilteredByTraineeAndDates() {

        Trainer trainer = new Trainer();

        Trainee trainee = new Trainee();

        trainee.setUsername("alex.brown");

        Calendar calendar = Calendar.getInstance();

        calendar.set(2026, Calendar.JANUARY, 15);
        Date trainingDate = calendar.getTime();

        calendar.set(2026, Calendar.JANUARY, 1);
        Date fromDate = calendar.getTime();

        calendar.set(2026, Calendar.JANUARY, 31);
        Date toDate = calendar.getTime();

        Training training = new Training();

        training.setTrainee(trainee);

        training.setTrainingDate(trainingDate);

        List<Training> trainingList = new ArrayList<>();

        trainingList.add(training);

        trainer.setTrainings(trainingList);

        List<Training> result =
                trainerRepository.getTrainings(trainer, fromDate, toDate, "alex.brown");

        assertEquals(1, result.size());
    }

    @Test
    void shouldGetTrainersNotAssignedToTrainee() {

        List<Trainer> trainers =
                List.of(new Trainer());

        String hql = "SELECT t FROM Trainer t " +
                "WHERE t NOT IN (" +
                "  SELECT tr FROM Trainee tn JOIN tn.trainers tr WHERE tn.username = :username" +
                ")";

        when(em.createQuery(hql, Trainer.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter("username", "alex.brown"))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(trainers);

        List<Trainer> result =
                trainerRepository.getTrainersNotAssignedToTrainee("alex.brown");

        assertEquals(1, result.size());
    }

    @Test
    void shouldFindTrainersByUsernames() {

        List<String> usernames =
                List.of("ivan", "petr");

        List<Trainer> trainers =
                List.of(new Trainer(), new Trainer());

        when(em.createQuery("SELECT t FROM Trainer t WHERE t.username IN :usernames", Trainer.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter("usernames", usernames))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(trainers);

        List<Trainer> result =
                trainerRepository.findByUsernames(usernames);

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenUsernamesListIsEmpty() {

        List<Trainer> result =
                trainerRepository.findByUsernames(List.of());

        assertTrue(result.isEmpty());

        verify(em, never())
                .createQuery(anyString(), any());
    }
}