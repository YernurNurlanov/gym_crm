package com.crm.gym.storage;

import com.crm.gym.dao.TrainingDao;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TrainingStorageInitializerTest {

    private TrainingStorageInitializer initializer;

    private TrainingDao trainingDao;

    private Map<Long, Training> trainingStorage;

    private final String testFileName = "test_trainings.txt";

    @BeforeEach
    void setUp() {

        trainingDao = mock(TrainingDao.class);

        trainingStorage = new HashMap<>();

        initializer = new TrainingStorageInitializer();

        initializer.setTrainingDao(trainingDao);

        initializer.setTrainingStorage(trainingStorage);

        ReflectionTestUtils.setField(initializer, "trainingsFilePath", testFileName);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(testFileName));
        Files.deleteIfExists(Paths.get(testFileName + ".tmp"));
    }

    @Test
    void shouldLoadTrainingsFromExternalFile() throws IOException {

        String csvContent = "1,10,20,Yoga Session,YOGA,2023-10-10,60";
        Files.writeString(Paths.get(testFileName), csvContent);

        initializer.init();

        assertEquals(1, trainingStorage.size());

        Training loaded = trainingStorage.get(1L);

        assertEquals("Yoga Session", loaded.getTrainingName());

        assertEquals(60, loaded.getTrainingDuration());

        verify(trainingDao).setMaxId(1L);
    }

    @Test
    void shouldPersistTrainingsToExternalFile() throws IOException {

        Training training = new Training();
        training.setTrainingId(1L);
        training.setTrainerId(10L);
        training.setTraineeId(20L);
        training.setTrainingName("Power Lift");
        training.setTrainingType(TrainingType.STRENGTH);
        training.setTrainingDate(LocalDate.of(2023, 10, 10));
        training.setTrainingDuration(90);

        trainingStorage.put(1L, training);

        initializer.shutdown();

        Path path = Paths.get(testFileName);

        assertTrue(Files.exists(path));

        String content = Files.readString(path);

        assertTrue(content.contains("Power Lift"));

        assertTrue(content.contains("90"));
    }

    @Test
    void shouldSynchronizeIdGeneratorWithMaxId() {

        trainingStorage.put(10L, new Training());
        trainingStorage.put(55L, new Training());
        trainingStorage.put(30L, new Training());

        ReflectionTestUtils.invokeMethod(initializer, "synchronizeIdGenerator");

        verify(trainingDao).setMaxId(55L);
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {

        ReflectionTestUtils.setField(initializer, "trainingsFilePath", "non_existent.txt");

        assertThrows(RuntimeException.class, () -> initializer.init());
    }
}
