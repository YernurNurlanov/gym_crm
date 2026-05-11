package com.crm.gym.storage;

import com.crm.gym.dao.TrainerDao;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.TrainingType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class TrainerStorageInitializerTest {

    private TrainerStorageInitializer initializer;

    private TrainerDao trainerDao;

    private Map<Long, Trainer> trainerStorage;

    private final String testFileName = "test_trainers.csv";

    @BeforeEach
    void setUp() {

        trainerDao = mock(TrainerDao.class);

        trainerStorage = new HashMap<>();

        initializer = new TrainerStorageInitializer();

        initializer.setTrainerDao(trainerDao);

        initializer.setTrainerStorage(trainerStorage);

        ReflectionTestUtils.setField(initializer, "trainersFilePath", testFileName);
    }

    @AfterEach
    void tearDown() throws IOException {

        Files.deleteIfExists(Paths.get(testFileName));

        Files.deleteIfExists(Paths.get(testFileName + ".tmp"));
    }

    @Test
    void shouldLoadTrainersFromExternalFile() throws IOException {

        String csvContent = "1,Ivan,Ivanov,Ivan.Ivanov,pass,true,YOGA";

        Files.writeString(Paths.get(testFileName), csvContent);

        initializer.init();

        assertEquals(1, trainerStorage.size());

        Trainer loaded = trainerStorage.get(1L);

        assertEquals("Ivan", loaded.getFirstName());

        assertEquals(TrainingType.YOGA, loaded.getSpecialization());

        verify(trainerDao).setMaxId(1L);
    }

    @Test
    void shouldPersistTrainersToExternalFile() throws IOException {

        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        trainer.setFirstName("Alex");
        trainer.setLastName("Coach");
        trainer.setUsername("Alex.Coach");
        trainer.setPassword("secure");
        trainer.setActive(true);
        trainer.setSpecialization(TrainingType.CARDIO);

        trainerStorage.put(1L, trainer);

        initializer.shutdown();

        Path path = Paths.get(testFileName);

        assertTrue(Files.exists(path));

        String content = Files.readString(path);

        assertTrue(content.contains("Alex.Coach"));

        assertTrue(content.contains("CARDIO"));
    }

    @Test
    void shouldSynchronizeIdGeneratorWithMaxId() {

        trainerStorage.put(10L, new Trainer());
        trainerStorage.put(150L, new Trainer());
        trainerStorage.put(20L, new Trainer());

        ReflectionTestUtils.invokeMethod(initializer, "synchronizeIdGenerator");

        verify(trainerDao).setMaxId(150L);
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {

        ReflectionTestUtils.setField(initializer, "trainersFilePath", "missing_file.csv");

        assertThrows(RuntimeException.class, () -> initializer.init());
    }
}
