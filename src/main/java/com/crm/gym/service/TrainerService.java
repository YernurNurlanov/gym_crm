package com.crm.gym.service;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.repository.TrainingRepository;
import com.crm.gym.util.CredentialGenerator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final CredentialGenerator credentialGenerator;
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;

    public TrainerService(TrainerRepository trainerRepository, CredentialGenerator credentialGenerator, TraineeRepository traineeRepository, TrainingRepository trainingRepository) {
        this.trainerRepository = trainerRepository;
        this.credentialGenerator = credentialGenerator;
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(TrainerService.class);

    public Trainer createTrainer(@Valid Trainer trainer) {

        logger.info("Creating trainer: {} {}",
                trainer.getFirstName(),
                trainer.getLastName());

        trainer.setUsername(credentialGenerator.generateUniqueUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(credentialGenerator.generatePassword());

        trainer = trainerRepository.save(trainer);

        logger.info("Trainer created with id={}",
                trainer.getUserId());

        return trainer;
    }

    public void updateTrainer(@Valid Trainer trainer) {

        Optional<Trainer> existing =
                trainerRepository.findById(trainer.getUserId());

        if (existing.isEmpty()) {
            throw new RuntimeException("Trainer not found");
        }

        trainerRepository.save(trainer);
    }

    public void toggleTrainerStatus(Long id) {

        Optional<Trainer> trainer = trainerRepository.findById(id);
        if (trainer.isPresent()) {
            trainer.get().setActive(!trainer.get().isActive());
            trainerRepository.save(trainer.get());

            logger.info(
                    "Trainer id={} changed status to active={}",
                    id,
                    trainer.get().isActive());

        } else {

            logger.warn("Trainer id={} not found", id);

            throw new RuntimeException("Trainer not found");
        }

    }

    public Trainer changeTrainerPassword(Long trainerId, String newPassword) {
        Optional<Trainer> trainer = trainerRepository.findById(trainerId);

        if (trainer.isPresent()) {
            trainer.get().setPassword(newPassword);
            return trainerRepository.save(trainer.get());
        } else {
            throw new RuntimeException("Trainer not found");
        }
    }

    public List<Training> getTrainerTrainings(String trainerUsername, Date from, Date to, String traineeUsername) {

        Optional<Trainer> trainer = trainerRepository.findByUsername(trainerUsername);
        if (trainer.isEmpty()) {
            throw new RuntimeException("Trainer not found");
        }

        Optional<Trainee> trainee = traineeRepository.findByUsername(traineeUsername);
        if (trainee.isEmpty()) {
            throw new RuntimeException("Trainee not found");
        }

        return trainingRepository.findTrainerTrainings(trainerUsername, from, to, traineeUsername);
    }

    public Optional<Trainer> selectTrainer(Long id) {
        return trainerRepository.findById(id);
    }

    public Optional<Trainer> selectTrainerByUsername(String name) {
        return trainerRepository.findByUsername(name);
    }

    public List<Trainer> selectAllTrainers() {
        return trainerRepository.findAll();
    }

    public List<Trainer> getTrainersNotAssignedToTrainee(String username) {
        return trainerRepository.findActiveTrainersNotAssignedToTrainee(username);
    }

}
