package com.crm.gym.storage;

import com.crm.gym.dao.TrainingDao;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrainingStorageInitializer {

    @Value("${storage.trainings.path}")
    private String trainingsFilePath;

    private Map<Long, Training> trainingStorage;

    private TrainingDao trainingDao;

    private static final Logger logger =
            LoggerFactory.getLogger(
                    TrainingStorageInitializer.class);

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Autowired
    @Qualifier("trainingStorage")
    public void setTrainingStorage(
            Map<Long, Training> trainingStorage) {

        this.trainingStorage = trainingStorage;
    }

    @PostConstruct
    public void init() {

        Path externalPath = Paths.get(trainingsFilePath);
        InputStream inputStream;

        try {
            if (Files.exists(externalPath)) {
                logger.info(
                        "Loading trainings from external file: {}",
                        externalPath.toAbsolutePath());

                inputStream = Files.newInputStream(externalPath);
            } else {
                logger.info(
                        "External file not found, downloading from resources: {}",
                        trainingsFilePath);

                inputStream = getClass().getClassLoader().getResourceAsStream(trainingsFilePath);
            }

            if (inputStream == null) {
                throw new FileNotFoundException("The file was not found either in the root or in the resources: " + trainingsFilePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(",");

                    Training training = new Training();

                    training.setTrainingId(
                            Long.parseLong(parts[0]));

                    training.setTrainerId(
                            Long.parseLong(parts[1]));

                    training.setTraineeId(
                            Long.parseLong(parts[2]));

                    training.setTrainingName(parts[3]);

                    training.setTrainingType(
                            TrainingType.valueOf(parts[4]));

                    training.setTrainingDate(
                            LocalDate.parse(parts[5]));

                    training.setTrainingDuration(
                            Integer.parseInt(parts[6]));

                    trainingStorage.put(
                            training.getTrainingId(),
                            training);
                }
            }
            logger.info(
                    "Trainings downloaded successfully: {}",
                    trainingStorage.size());

            synchronizeIdGenerator();

        } catch (Exception e) {
            logger.error(
                    "Failed to load trainings file",
                    e);

            throw new RuntimeException("Failed to load trainings file", e);
        }
    }

    @PreDestroy
    public void shutdown() {

        logger.info(
                "Persisting {} trainings to file",
                trainingStorage.size());

        Path path = Paths.get(trainingsFilePath);
        Path temp = Paths.get(trainingsFilePath + ".tmp");

        try {

            String data = trainingStorage.values().stream()
                    .map(this::serializeTraining)
                    .collect(Collectors.joining(System.lineSeparator()));
            Files.writeString(temp, data,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(temp, path,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);

            logger.info("Trainings successfully persisted");

        } catch (IOException e) {
            throw new RuntimeException("Failed to save trainees file", e);
        }
    }

    private String serializeTraining(Training training) {
        return String.join(",",
                String.valueOf(training.getTrainingId()),
                String.valueOf(training.getTrainerId()),
                String.valueOf(training.getTraineeId()),
                training.getTrainingName(),
                training.getTrainingType().toString(),
                training.getTrainingDate().toString(),
                String.valueOf(training.getTrainingDuration())
        );
    }

    private void synchronizeIdGenerator() {
        long maxId = trainingStorage.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        trainingDao.setMaxId(maxId);
    }
}
