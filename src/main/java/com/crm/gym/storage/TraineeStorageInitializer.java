package com.crm.gym.storage;

import com.crm.gym.dao.TraineeDao;
import com.crm.gym.entity.Trainee;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TraineeStorageInitializer {

    @Value("${storage.trainees.path}")
    private String traineesFilePath;

    private Map<Long, Trainee> traineeStorage;

    private TraineeDao traineeDao;

    private static final Logger logger =
            LoggerFactory.getLogger(
                    TraineeStorageInitializer.class);

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    @Qualifier("traineeStorage")
    public void setTraineeStorage(
            Map<Long, Trainee> traineeStorage) {

        this.traineeStorage = traineeStorage;
    }

    @PostConstruct
    public void init() {

        Path externalPath = Paths.get(traineesFilePath);
        InputStream inputStream;

        try {
            if (Files.exists(externalPath)) {
                logger.info(
                        "Loading trainees from external file: {}",
                        externalPath.toAbsolutePath());
                inputStream = Files.newInputStream(externalPath);
            } else {
                logger.info(
                        "External file not found, downloading from resources: {}",
                        traineesFilePath);

                inputStream = getClass().getClassLoader().getResourceAsStream(traineesFilePath);
            }

            if (inputStream == null) {
                throw new FileNotFoundException("The file was not found either in the root or in the resources: " + traineesFilePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    Trainee trainee = getTrainee(line);

                    traineeStorage.put(trainee.getUserId(), trainee);
                }
            }
            logger.info(
                    "Trainees downloaded successfully: {}",
                    traineeStorage.size());

            synchronizeIdGenerator();

        } catch (Exception e) {
            logger.error(
                    "Failed to load trainees file",
                    e);

            throw new RuntimeException("Failed to load trainees file", e);
        }

    }

    private static Trainee getTrainee(String line) {
        String[] parts = line.split(",");

        Trainee trainee = new Trainee();

        trainee.setUserId(Long.parseLong(parts[0]));
        trainee.setFirstName(parts[1]);
        trainee.setLastName(parts[2]);
        trainee.setUsername(parts[3]);
        trainee.setPassword(parts[4]);
        trainee.setActive(Boolean.parseBoolean(parts[5]));
        trainee.setDateOfBirth(
                LocalDate.parse(parts[6]));
        trainee.setAddress(parts[7]);
        return trainee;
    }

    @PreDestroy
    public void shutdown() {

        logger.info(
                "Persisting {} trainees to file",
                traineeStorage.size());

        Path path = Paths.get(traineesFilePath);
        Path temp = Paths.get(traineesFilePath + ".tmp");

        try {

            String data = traineeStorage.values().stream()
                    .map(this::serializeTrainee)
                    .collect(Collectors.joining(System.lineSeparator()));

            Files.writeString(temp, data,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(temp, path,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);

            logger.info("Trainees successfully persisted");

        } catch (IOException e) {
            throw new RuntimeException("Failed to save trainees file", e);
        }
    }

    private String serializeTrainee(Trainee t) {
        return String.join(",",
                String.valueOf(t.getUserId()),
                t.getFirstName(),
                t.getLastName(),
                t.getUsername(),
                t.getPassword(),
                String.valueOf(t.isActive()),
                t.getDateOfBirth().toString(),
                t.getAddress()
        );
    }

    private void synchronizeIdGenerator() {
        long maxId = traineeStorage.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        traineeDao.setMaxId(maxId);
    }
}