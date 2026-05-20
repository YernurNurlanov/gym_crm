package com.crm.gym.dao;

import com.crm.gym.entity.Trainee;
import com.crm.gym.util.CredentialGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TraineeDaoTest {

    private TraineeDao traineeDao;
    private CredentialGenerator credentialGenerator;
    private Map<Long, Trainee> traineeStorage;

    @BeforeEach
    void setUp() {
        credentialGenerator = mock(CredentialGenerator.class);

        traineeStorage = new HashMap<>();

        traineeDao = new TraineeDao(traineeStorage);
        traineeDao.setCredentialGenerator(credentialGenerator);
    }

    @Test
    void shouldCreateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("Ivan");
        trainee.setLastName("Ivanov");

        when(credentialGenerator.generateUniqueUsername("Ivan", "Ivanov"))
                .thenReturn("Ivan.Ivanov");
        when(credentialGenerator.generatePassword())
                .thenReturn("password123");

        Trainee created = traineeDao.create(trainee);

        assertNotNull(created);
        assertEquals(1L, created.getUserId());
        assertEquals("Ivan.Ivanov", created.getUsername());
        assertEquals("password123", created.getPassword());

        assertTrue(traineeStorage.containsKey(1L));
    }

    @Test
    void shouldSelectTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        traineeStorage.put(1L, trainee);

        Trainee selected = traineeDao.select(1L);

        assertNotNull(selected);
        assertEquals(1L, selected.getUserId());
    }

    @Test
    void shouldReturnNullWhenTraineeNotFound() {
        Trainee selected = traineeDao.select(99L);

        assertNull(selected);
    }

    @Test
    void shouldUpdateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setFirstName("OldName");
        traineeStorage.put(1L, trainee);

        trainee.setFirstName("NewName");
        traineeDao.update(trainee);

        assertEquals("NewName", traineeStorage.get(1L).getFirstName());
    }

    @Test
    void shouldDeleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        traineeStorage.put(1L, trainee);

        traineeDao.delete(1L);

        assertFalse(traineeStorage.containsKey(1L));
    }

    @Test
    void shouldSelectAllTrainees() {
        traineeStorage.put(1L, new Trainee());
        traineeStorage.put(2L, new Trainee());

        List<Trainee> result = traineeDao.selectAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldGenerateIdStartingFromMaxId() {
        traineeDao.setMaxId(100L);

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        when(credentialGenerator.generateUniqueUsername(any(), any()))
                .thenReturn("john.doe");

        Trainee created = traineeDao.create(trainee);

        assertEquals(101L, created.getUserId());
    }
}