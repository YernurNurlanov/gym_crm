package com.crm.gym.util;

import com.crm.gym.dao.TraineeDao;
import com.crm.gym.dao.TrainerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CredentialGeneratorTest {

    private CredentialGenerator credentialGenerator;

    @BeforeEach
    void setUp() {

        TrainerDao trainerDao = mock(TrainerDao.class);

        TraineeDao traineeDao = mock(TraineeDao.class);

        credentialGenerator = new CredentialGenerator();

        credentialGenerator.setTrainerDao(trainerDao);

        credentialGenerator.setTraineeDao(traineeDao);
    }

    @Test
    void shouldGeneratePasswordWithLength10() {

        String password =
                credentialGenerator.generatePassword();

        assertEquals(10, password.length());
    }

    @Test
    void shouldGenerateUsername() {

        String username =
                credentialGenerator.generateUniqueUsername(
                        "John",
                        "Smith");

        assertEquals(
                "John.Smith",
                username);
    }

}
