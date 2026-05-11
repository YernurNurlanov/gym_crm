package com.crm.gym.storage;

import com.crm.gym.dao.TraineeDao;
import com.crm.gym.entity.Trainee;
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

class TraineeStorageInitializerTest {

    private TraineeStorageInitializer initializer;

    private TraineeDao traineeDao;

    private Map<Long, Trainee> traineeStorage;

    private final String testFileName = "test_trainees.csv";

    @BeforeEach
    void setUp() {

        traineeDao = mock(TraineeDao.class);

        traineeStorage = new HashMap<>();

        initializer = new TraineeStorageInitializer();

        initializer.setTraineeDao(traineeDao);

        initializer.setTraineeStorage(traineeStorage);

        ReflectionTestUtils.setField(initializer, "traineesFilePath", testFileName);
    }

    @AfterEach
    void tearDown() throws IOException {

        Files.deleteIfExists(Paths.get(testFileName));

        Files.deleteIfExists(Paths.get(testFileName + ".tmp"));
    }

    @Test
    void shouldLoadTraineesFromExternalFile() throws IOException {

        String csvContent = "1,John,Doe,John.Doe,pass,true,2000-01-01,123 Street";

        Files.writeString(Paths.get(testFileName), csvContent);

        initializer.init();

        assertEquals(1, traineeStorage.size());

        Trainee loaded = traineeStorage.get(1L);

        assertEquals("John", loaded.getFirstName());

        assertEquals(LocalDate.of(2000, 1, 1), loaded.getDateOfBirth());

        assertEquals("123 Street", loaded.getAddress());

        verify(traineeDao).setMaxId(1L);
    }

    @Test
    void shouldPersistTraineesToExternalFile() throws IOException {

        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setFirstName("Jane");
        trainee.setLastName("Smith");
        trainee.setUsername("Jane.Smith");
        trainee.setPassword("secret");
        trainee.setActive(false);
        trainee.setDateOfBirth(LocalDate.of(1995, 5, 15));
        trainee.setAddress("456 Avenue");

        traineeStorage.put(1L, trainee);

        initializer.shutdown();

        Path path = Paths.get(testFileName);

        assertTrue(Files.exists(path));

        String content = Files.readString(path);

        assertTrue(content.contains("Jane.Smith"));

        assertTrue(content.contains("1995-05-15"));

        assertTrue(content.contains("456 Avenue"));
    }

    @Test
    void shouldSynchronizeIdGeneratorWithMaxId() {

        traineeStorage.put(5L, new Trainee());
        traineeStorage.put(200L, new Trainee());
        traineeStorage.put(100L, new Trainee());

        ReflectionTestUtils.invokeMethod(initializer, "synchronizeIdGenerator");

        verify(traineeDao).setMaxId(200L);
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {

        ReflectionTestUtils.setField(initializer, "traineesFilePath", "unknown_path.csv");

        assertThrows(RuntimeException.class, () -> initializer.init());
    }
}
