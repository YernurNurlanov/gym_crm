package com.crm.gym.service;

import com.crm.gym.dao.TrainingDao;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private TrainingDao trainingDao;

    private TrainingService trainingService;

    @BeforeEach
    void setUp() {

        trainingDao = mock(TrainingDao.class);

        trainingService = new TrainingService();

        trainingService.setTrainingDao(trainingDao);
    }

    @Test
    void shouldCreateTraining() {

        Training training = new Training();

        training.setTrainingId(1L);
        training.setTrainerId(10L);
        training.setTraineeId(20L);
        training.setTrainingName("Morning Cardio");
        training.setTrainingType(TrainingType.CARDIO);
        training.setTrainingDate(LocalDate.now());
        training.setTrainingDuration(60);

        when(trainingDao.create(training))
                .thenReturn(training);

        Training created =
                trainingService.createTraining(training);

        assertNotNull(created);

        assertEquals(
                1L,
                created.getTrainingId());

        assertEquals(
                "Morning Cardio",
                created.getTrainingName());

        verify(trainingDao, times(1))
                .create(training);
    }

    @Test
    void shouldSelectTraining() {

        Training training = new Training();

        training.setTrainingId(1L);

        when(trainingDao.select(1L))
                .thenReturn(training);

        Training selected =
                trainingService.selectTraining(1L);

        assertEquals(training, selected);

        verify(trainingDao)
                .select(1L);
    }

    @Test
    void shouldSelectAllTrainings() {

        List<Training> trainings =
                List.of(
                        new Training(),
                        new Training());

        when(trainingDao.selectAll())
                .thenReturn(trainings);

        List<Training> result =
                trainingService.selectAllTrainings();

        assertEquals(2, result.size());

        verify(trainingDao)
                .selectAll();
    }
}
