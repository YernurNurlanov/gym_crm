package com.crm.gym.service;

import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private TrainingRepository trainingRepository;

    private TrainingService trainingService;

    @BeforeEach
    void setUp() {

        trainingRepository = mock(TrainingRepository.class);

        trainingService = new TrainingService(trainingRepository);
    }

    @Test
    void shouldCreateTraining() {

        Trainer trainer = new Trainer();

        trainer.setUserId(10L);

        Training training = new Training();

        training.setTrainingName("Powerlifting");

        training.setTrainer(trainer);

        Training savedTraining = new Training();

        savedTraining.setTrainingId(1L);

        savedTraining.setTrainingName("Powerlifting");

        savedTraining.setTrainer(trainer);

        when(trainingRepository.save(training))
                .thenReturn(savedTraining);

        Training result =
                trainingService.createTraining(training);

        assertNotNull(result);

        assertEquals(1L, result.getTrainingId());

        assertEquals("Powerlifting", result.getTrainingName());

        assertEquals(10L, result.getTrainer().getUserId());

        verify(trainingRepository, times(1))
                .save(training);
    }
}