package com.crm.gym;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.service.TraineeService;
import com.crm.gym.service.TrainerService;
import com.crm.gym.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymFacade {

    private final TrainerService trainerService;

    private final TraineeService traineeService;

    private final TrainingService trainingService;

    public GymFacade(TrainerService trainerService,
                     TraineeService traineeService,
                     TrainingService trainingService) {

        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    // Trainer operations

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    public Trainer selectTrainer(Long id) {
        return trainerService.selectTrainer(id);
    }

    public List<Trainer> selectAllTrainers() {
        return trainerService.selectAllTrainers();
    }

    public void updateTrainer(Trainer trainer) {
        trainerService.updateTrainer(trainer);
    }

    // Trainee operations

    public Trainee createTrainee(Trainee trainee) {
        return traineeService.createTrainee(trainee);
    }

    public Trainee selectTrainee(Long id) {
        return traineeService.selectTrainee(id);
    }

    public List<Trainee> selectAllTrainees() {
        return traineeService.selectAllTrainees();
    }

    public void updateTrainee(Trainee trainee) {
        traineeService.updateTrainee(trainee);
    }

    public void deleteTrainee(Long id) {
        traineeService.deleteTrainee(id);
    }

    // Training operations

    public Training createTraining(Training training) {
        return trainingService.createTraining(training);
    }

    public Training selectTraining(Long id) {
        return trainingService.selectTraining(id);
    }

    public List<Training> selectAllTrainings() {
        return trainingService.selectAllTrainings();
    }
}
