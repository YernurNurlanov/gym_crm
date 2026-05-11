package com.crm.gym.dao;

import com.crm.gym.entity.Trainer;
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

class TrainerDaoTest {

    private TrainerDao trainerDao;

    private CredentialGenerator credentialGenerator;

    private Map<Long, Trainer> trainerStorage;

    @BeforeEach
    void setUp() {

        credentialGenerator = mock(CredentialGenerator.class);

        trainerStorage = new HashMap<>();

        trainerDao = new TrainerDao(trainerStorage);

        trainerDao.setCredentialGenerator(credentialGenerator);
    }

    @Test
    void shouldCreateTrainer() {

        Trainer trainer = new Trainer();
        trainer.setFirstName("Alex");
        trainer.setLastName("Coach");

        when(credentialGenerator.generateUniqueUsername("Alex", "Coach"))
                .thenReturn("Alex.Coach");

        when(credentialGenerator.generatePassword())
                .thenReturn("pass123");

        Trainer created = trainerDao.create(trainer);

        assertNotNull(created);

        assertEquals(1L, created.getUserId());

        assertEquals("Alex.Coach", created.getUsername());

        assertEquals("pass123", created.getPassword());

        assertTrue(trainerStorage.containsKey(1L));
    }

    @Test
    void shouldUpdateTrainer() {

        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        trainer.setFirstName("OldName");

        trainerStorage.put(1L, trainer);

        trainer.setFirstName("NewName");

        trainerDao.update(trainer);

        assertEquals("NewName", trainerStorage.get(1L).getFirstName());
    }

    @Test
    void shouldSelectTrainer() {

        Trainer trainer = new Trainer();
        trainer.setUserId(1L);

        trainerStorage.put(1L, trainer);

        Trainer selected = trainerDao.select(1L);

        assertNotNull(selected);

        assertEquals(1L, selected.getUserId());
    }

    @Test
    void shouldReturnNullWhenTrainerNotFound() {

        Trainer selected = trainerDao.select(99L);

        assertNull(selected);
    }

    @Test
    void shouldSelectAllTrainers() {

        trainerStorage.put(1L, new Trainer());
        trainerStorage.put(2L, new Trainer());

        List<Trainer> result = trainerDao.selectAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldStartIdFromMaxId() {

        trainerDao.setMaxId(50L);

        Trainer trainer = new Trainer();
        trainer.setFirstName("Test");
        trainer.setLastName("User");

        when(credentialGenerator.generateUniqueUsername(any(), any()))
                .thenReturn("test.user");

        Trainer created = trainerDao.create(trainer);

        assertEquals(51L, created.getUserId());
    }
}
