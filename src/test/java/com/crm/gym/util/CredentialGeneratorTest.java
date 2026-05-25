package com.crm.gym.util;

import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CredentialGeneratorTest {

    private CredentialGenerator credentialGenerator;

    @BeforeEach
    void setUp() {

        TrainerRepository trainerRepository = mock(TrainerRepository.class);

        TraineeRepository traineeRepository = mock(TraineeRepository.class);

        credentialGenerator = new CredentialGenerator(traineeRepository, trainerRepository);
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
