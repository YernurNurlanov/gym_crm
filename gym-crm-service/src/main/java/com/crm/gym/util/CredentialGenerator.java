package com.crm.gym.util;

import com.crm.gym.entity.User;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CredentialGenerator {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int PASSWORD_LENGTH = 10;

    private final SecureRandom random = new SecureRandom();

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    public CredentialGenerator(TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    public String generateUniqueUsername(String firstName, String lastName) {

        Set<String> existingUsernames = Stream.concat(
                trainerRepository.findAll().stream().map(User::getUsername),
                traineeRepository.findAll().stream().map(User::getUsername)
        ).collect(Collectors.toSet());

        int serialNumber = 0;

        while (true) {

            String username = generateUsername(
                    firstName,
                    lastName,
                    serialNumber
            );

            if (!existingUsernames.contains(username)) {
                return username;
            }

            serialNumber++;
        }
    }

    private String generateUsername(String firstName, String lastName, int serialNumber) {

        String baseUsername = firstName + "." + lastName;

        if (serialNumber == 0) {
            return baseUsername;
        }

        return baseUsername + serialNumber;
    }

    public String generatePassword() {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {

            int index = random.nextInt(CHARACTERS.length());

            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
