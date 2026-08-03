package com.crm.gym.service;

import com.crm.gym.dto.AddTrainingRequest;
import com.crm.gym.dto.TrainerWorkloadRequest;
import com.crm.gym.entity.ActionType;
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
    private final TrainerWorkloadProducer trainerWorkloadProducer;

    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainerWorkloadProducer trainerWorkloadProducer) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainerWorkloadProducer = trainerWorkloadProducer;
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

        Trainee traineeEntity = trainee.get();
        Trainer trainerEntity = trainer.get();

        Training training = new Training();
        training.setTrainee(traineeEntity);
        training.setTrainer(trainerEntity);
        training.setName(request.getTrainingName());
        training.setDate(request.getTrainingDate());
        training.setDuration(request.getTrainingDuration());

        trainingRepository.save(training);

        TrainerWorkloadRequest workloadRequest =
                new TrainerWorkloadRequest(
                        trainerEntity.getUsername(),
                        trainerEntity.getFirstName(),
                        trainerEntity.getLastName(),
                        trainerEntity.isActive(),
                        training.getDate(),
                        training.getDuration(),
                        ActionType.ADD
                );

        trainerWorkloadProducer.send(workloadRequest);

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteTraining(Long trainingId) {
        Optional<Training> training = trainingRepository.findById(trainingId);

        if (training.isEmpty()) {
            throw new NotFoundException("Training with id " + trainingId + " not found");
        }

        Training trainingEntity = training.get();

        trainingRepository.delete(trainingEntity);

        TrainerWorkloadRequest workloadRequest =
                new TrainerWorkloadRequest(
                        trainingEntity.getTrainer().getUsername(),
                        trainingEntity.getTrainer().getFirstName(),
                        trainingEntity.getTrainer().getLastName(),
                        trainingEntity.getTrainer().isActive(),
                        trainingEntity.getDate(),
                        trainingEntity.getDuration(),
                        ActionType.DELETE
                );

        trainerWorkloadProducer.send(workloadRequest);

        return ResponseEntity.ok().build();
    }
}
