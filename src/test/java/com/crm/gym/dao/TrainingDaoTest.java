package com.crm.gym.dao;

import com.crm.gym.entity.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDaoTest {

    private TrainingDao trainingDao;

    private Map<Long, Training> trainingStorage;

    @BeforeEach
    void setUp() {

        trainingStorage = new HashMap<>();

        trainingDao = new TrainingDao(trainingStorage);
    }

    @Test
    void shouldCreateTraining() {

        Training training = new Training();
        training.setTrainingName("Java Basics");

        Training created = trainingDao.create(training);

        assertNotNull(created);

        assertEquals(1L, created.getTrainingId());

        assertEquals("Java Basics", created.getTrainingName());

        assertTrue(trainingStorage.containsKey(1L));
    }

    @Test
    void shouldSelectTraining() {

        Training training = new Training();
        training.setTrainingId(10L);

        trainingStorage.put(10L, training);

        Training selected = trainingDao.select(10L);

        assertNotNull(selected);

        assertEquals(10L, selected.getTrainingId());
    }

    @Test
    void shouldReturnNullWhenTrainingNotFound() {

        Training selected = trainingDao.select(999L);

        assertNull(selected);
    }

    @Test
    void shouldSelectAllTrainings() {

        trainingStorage.put(1L, new Training());
        trainingStorage.put(2L, new Training());

        List<Training> result = trainingDao.selectAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldStartIdFromMaxId() {

        trainingDao.setMaxId(500L);

        Training training = new Training();

        Training created = trainingDao.create(training);

        assertEquals(501L, created.getTrainingId());
    }
}
