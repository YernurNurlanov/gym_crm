package com.crm.gym.service;

import com.crm.gym.dao.TraineeDao;
import com.crm.gym.entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    private TraineeDao traineeDao;

    private TraineeService traineeService;

    @BeforeEach
    void setUp() {

        traineeDao = mock(TraineeDao.class);

        traineeService = new TraineeService();

        traineeService.setTraineeDao(traineeDao);
    }

    @Test
    void shouldCreateTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeDao.create(trainee))
                .thenReturn(trainee);

        Trainee created =
                traineeService.createTrainee(trainee);

        assertNotNull(created);

        assertEquals(1L, created.getUserId());

        verify(traineeDao, times(1))
                .create(trainee);
    }

    @Test
    void shouldUpdateTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeDao.select(1L))
                .thenReturn(trainee);

        traineeService.updateTrainee(trainee);

        verify(traineeDao)
                .update(trainee);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeDao.select(1L))
                .thenReturn(null);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> traineeService.updateTrainee(trainee));

        assertEquals(
                "Trainee not found",
                exception.getMessage());

        verify(traineeDao, never())
                .update(any());
    }

    @Test
    void shouldDeleteTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeDao.select(1L))
                .thenReturn(trainee);

        traineeService.deleteTrainee(1L);

        verify(traineeDao)
                .delete(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingTrainee() {

        when(traineeDao.select(1L))
                .thenReturn(null);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> traineeService.deleteTrainee(1L));

        assertEquals(
                "Trainee not found",
                exception.getMessage());

        verify(traineeDao, never())
                .delete(any());
    }

    @Test
    void shouldSelectTrainee() {

        Trainee trainee = new Trainee();

        trainee.setUserId(1L);

        when(traineeDao.select(1L))
                .thenReturn(trainee);

        Trainee selected =
                traineeService.selectTrainee(1L);

        assertEquals(trainee, selected);
    }

    @Test
    void shouldSelectAllTrainees() {

        List<Trainee> trainees =
                List.of(
                        new Trainee(),
                        new Trainee());

        when(traineeDao.selectAll())
                .thenReturn(trainees);

        List<Trainee> result =
                traineeService.selectAllTrainees();

        assertEquals(2, result.size());
    }
}