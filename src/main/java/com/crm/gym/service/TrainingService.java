package com.crm.gym.service;

import com.crm.gym.dto.AddTrainingRequest;
import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.exception.NotFoundException;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.repository.TrainingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Transactional
    public ResponseEntity<Void> addTraining(AddTrainingRequest request) {

        Optional<Trainee> trainee = traineeRepository.findByUsername(request.getTraineeUsername());
        if (trainee.isEmpty()) {
            throw new NotFoundException("Trainee with username " + request.getTraineeUsername() + " not found");
        }

        Optional<Trainer> trainer = trainerRepository.findByUsername(request.getTrainerUsername());
        if (trainer.isEmpty()) {
            throw new NotFoundException("Trainer with username " + request.getTrainerUsername() + " not found");
        }

        Training training = new Training();
        training.setTrainee(trainee.get());
        training.setTrainer(trainer.get());
        training.setName(request.getTrainingName());
        training.setDate(request.getTrainingDate());
        training.setDuration(request.getTrainingDuration());

        trainingRepository.save(training);

        return ResponseEntity.ok().build();
    }
}
