package com.crm.gym.repository;

import com.crm.gym.entity.Training;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingRepositoryTest {

    private EntityManager em;

    private TrainingRepository trainingRepository;

    @BeforeEach
    void setUp() {

        em = mock(EntityManager.class);

        trainingRepository = new TrainingRepository(em);
    }

    @Test
    void shouldPersistTraining() {

        Training training = new Training();

        training.setTrainingName("Crossfit");

        Training result =
                trainingRepository.save(training);

        assertNotNull(result);

        assertEquals("Crossfit", result.getTrainingName());

        verify(em, times(1))
                .persist(training);
    }
}