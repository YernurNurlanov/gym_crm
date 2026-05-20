package com.crm.gym.util;

import com.crm.gym.dao.TraineeDao;
import com.crm.gym.dao.TrainerDao;
import com.crm.gym.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    private TraineeDao traineeDao;

    private TrainerDao trainerDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    private String generateUsername(String firstName,
                                   String lastName,
                                   int serialNumber) {

        String baseUsername = firstName + "." + lastName;

        if (serialNumber == 0) {
            return baseUsername;
        }

        return baseUsername + serialNumber;
    }

    public String generateUniqueUsername(String firstName, String lastName) {

        Set<String> existingUsernames = Stream.concat(
                trainerDao.selectAll().stream().map(User::getUsername),
                traineeDao.selectAll().stream().map(User::getUsername)
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

    public String generatePassword() {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {

            int index = random.nextInt(CHARACTERS.length());

            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
