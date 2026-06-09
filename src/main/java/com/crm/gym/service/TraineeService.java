package com.crm.gym.service;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.repository.TrainingRepository;
import com.crm.gym.util.CredentialGenerator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final CredentialGenerator credentialGenerator;
    private final TrainingRepository trainingRepository;

    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository, CredentialGenerator credentialGenerator, TrainingRepository trainingRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.credentialGenerator = credentialGenerator;
        this.trainingRepository = trainingRepository;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(TraineeService.class);

    public Trainee createTrainee(@Valid Trainee trainee) {

        logger.info("Creating trainee: {} {}",
                trainee.getFirstName(),
                trainee.getLastName());

        trainee.setUsername(credentialGenerator.generateUniqueUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(credentialGenerator.generatePassword());

        trainee = traineeRepository.save(trainee);

        logger.info("Trainee created with id={}",
                trainee.getUserId());

        return trainee;
    }

    public void updateTrainee(@Valid Trainee trainee) {

        Optional<Trainee> existing =
                traineeRepository.findById(trainee.getUserId());

        if (existing.isEmpty()) {
            throw new RuntimeException("Trainee not found");
        }

        traineeRepository.save(trainee);
    }

    public Trainee changeTraineePassword(Long traineeId, String newPassword) {
        Optional<Trainee> trainee = traineeRepository.findById(traineeId);

        if (trainee.isPresent()) {
            trainee.get().setPassword(newPassword);
            return traineeRepository.save(trainee.get());
        } else {
            throw new RuntimeException("Trainee not found");
        }
    }

    public void toggleTraineeStatus(Long id) {

        Optional<Trainee> trainee = traineeRepository.findById(id);
        if (trainee.isPresent()) {
            trainee.get().setActive(!trainee.get().isActive());
            traineeRepository.save(trainee.get());

            logger.info(
                    "Trainee id={} changed status to active={}",
                    id,
                    trainee.get().isActive());

        } else {

            logger.warn("Trainee id={} not found", id);

            throw new RuntimeException("Trainee not found");
        }

    }

    public Optional<Trainee> selectTrainee(Long id) {
        return traineeRepository.findById(id);
    }

    public void deleteTrainee(String username) {

        Optional<Trainee> trainee = traineeRepository.findByUsername(username);

        if (trainee.isEmpty()) {
            throw new RuntimeException("Trainee not found");
        }

        traineeRepository.delete(trainee.get());
    }

    public List<Training> getTraineeTrainings(String traineeUsername, Date from, Date to, String trainerUsername, TrainingType trainingType) {

        Optional<Trainee> trainee = traineeRepository.findByUsername(traineeUsername);
        if (trainee.isEmpty()) {
            throw new RuntimeException("Trainee not found");
        }

        Optional<Trainer> trainer = trainerRepository.findByUsername(trainerUsername);
        if (trainer.isEmpty()) {
            throw new RuntimeException("Trainer not found");
        }

        return trainingRepository.findTraineeTrainings(traineeUsername,from, to, trainerUsername, trainingType);
    }

    public Optional<Trainee> selectTraineeByUsername(String name) {
        return traineeRepository.findByUsername(name);
    }


    public List<Trainee> selectAllTrainees() {
        return traineeRepository.findAll();
    }

    @Transactional
    public List<Trainer> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {

        Trainee trainee = traineeRepository.findByUsername(traineeUsername)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found: " + traineeUsername));

        List<Trainer> newTrainers = trainerRepository.findByUsernameIn(trainerUsernames);

        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(newTrainers);

        return traineeRepository.save(trainee).getTrainers();
    }

}
