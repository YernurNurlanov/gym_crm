package com.crm.gym.service;

import com.crm.gym.dao.TrainerDao;
import com.crm.gym.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerDao trainerDao;

    private TrainerService trainerService;

    @BeforeEach
    void setUp() {

        trainerDao = mock(TrainerDao.class);

        trainerService = new TrainerService();

        trainerService.setTrainerDao(trainerDao);
    }

    @Test
    void shouldCreateTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(trainerDao.create(trainer))
                .thenReturn(trainer);

        Trainer created =
                trainerService.createTrainer(trainer);

        assertNotNull(created);

        assertEquals(1L, created.getUserId());

        verify(trainerDao, times(1))
                .create(trainer);
    }

    @Test
    void shouldUpdateTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(trainerDao.select(1L))
                .thenReturn(trainer);

        trainerService.updateTrainer(trainer);

        verify(trainerDao)
                .update(trainer);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(trainerDao.select(1L))
                .thenReturn(null);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> trainerService.updateTrainer(trainer));

        assertEquals(
                "Trainer not found",
                exception.getMessage());

        verify(trainerDao, never())
                .update(any());
    }

    @Test
    void shouldSelectTrainer() {

        Trainer trainer = new Trainer();

        trainer.setUserId(1L);

        when(trainerDao.select(1L))
                .thenReturn(trainer);

        Trainer selected =
                trainerService.selectTrainer(1L);

        assertEquals(trainer, selected);
    }

    @Test
    void shouldSelectAllTrainers() {

        List<Trainer> trainers =
                List.of(
                        new Trainer(),
                        new Trainer());

        when(trainerDao.selectAll())
                .thenReturn(trainers);

        List<Trainer> result =
                trainerService.selectAllTrainers();

        assertEquals(2, result.size());
    }
}