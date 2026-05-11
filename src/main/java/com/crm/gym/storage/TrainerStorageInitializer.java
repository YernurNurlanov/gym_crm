package com.crm.gym.storage;

import com.crm.gym.dao.TrainerDao;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.TrainingType;
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
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrainerStorageInitializer {

    @Value("${storage.trainers.path}")
    private String trainersFilePath;

    private Map<Long, Trainer> trainerStorage;

    private TrainerDao trainerDao;

    private static final Logger logger =
            LoggerFactory.getLogger(
                    TrainerStorageInitializer.class);

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    @Qualifier("trainerStorage")
    public void setTrainerStorage(
            Map<Long, Trainer> trainerStorage) {

        this.trainerStorage = trainerStorage;
    }

    @PostConstruct
    public void init() {
        Path externalPath = Paths.get(trainersFilePath);
        InputStream inputStream;

        try {
            if (Files.exists(externalPath)) {
                logger.info(
                        "Loading trainers from external file: {}",
                        externalPath.toAbsolutePath());
                inputStream = Files.newInputStream(externalPath);
            } else {
                logger.info(
                        "External file not found, downloading from resources: {}",
                        trainersFilePath);
                inputStream = getClass().getClassLoader().getResourceAsStream(trainersFilePath);
            }

            if (inputStream == null) {
                throw new FileNotFoundException("The file was not found either in the root or in the resources: " + trainersFilePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(",");
                    Trainer trainer = new Trainer();

                    trainer.setUserId(Long.parseLong(parts[0].trim()));
                    trainer.setFirstName(parts[1].trim());
                    trainer.setLastName(parts[2].trim());
                    trainer.setUsername(parts[3].trim());
                    trainer.setPassword(parts[4].trim());
                    trainer.setActive(Boolean.parseBoolean(parts[5].trim()));

                    if (parts.length > 6) {
                        trainer.setSpecialization(TrainingType.valueOf(parts[6].trim()));
                    }

                    trainerStorage.put(trainer.getUserId(), trainer);
                }
            }
            logger.info(
                    "Trainers downloaded successfully: {}",
                    trainerStorage.size());

            synchronizeIdGenerator();

        } catch (Exception e) {
            logger.error(
                    "Failed to load trainers file",
                    e);

            throw new RuntimeException("Failed to load trainers file", e);
        }
    }

    @PreDestroy
    public void shutdown() {

        logger.info(
                "Persisting {} trainers to file",
                trainerStorage.size());

        Path path = Paths.get(trainersFilePath);
        Path temp = Paths.get(trainersFilePath + ".tmp");

        try {

            String data = trainerStorage.values().stream()
                    .map(this::serializeTrainer)
                    .collect(Collectors.joining(System.lineSeparator()));
            Files.writeString(temp, data,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(temp, path,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);

            logger.info("Trainers successfully persisted");

        } catch (IOException e) {
            throw new RuntimeException("Failed to save trainees file", e);
        }
    }

    private String serializeTrainer(Trainer t) {

        return String.join(",",
                String.valueOf(t.getUserId()),
                t.getFirstName(),
                t.getLastName(),
                t.getUsername(),
                t.getPassword(),
                String.valueOf(t.isActive()),
                t.getSpecialization().toString()
        );
    }

    private void synchronizeIdGenerator() {
        long maxId = trainerStorage.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        trainerDao.setMaxId(maxId);
    }
}
