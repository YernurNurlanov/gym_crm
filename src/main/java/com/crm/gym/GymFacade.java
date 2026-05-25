package com.crm.gym;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import com.crm.gym.service.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {

    private final TrainerService trainerService;

    private final TraineeService traineeService;

    private final TrainingService trainingService;

    private final TrainingTypeService trainingTypeService;

    private final AuthenticationService authenticationService;

    public GymFacade(TrainerService trainerService,
                     TraineeService traineeService,
                     TrainingService trainingService, TrainingTypeService trainingTypeService, AuthenticationService authenticationService) {

        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.authenticationService = authenticationService;
    }

    // Authentication operations

    public boolean authenticate(String username, String password) {
        return authenticationService.authenticate(username, password);
    }

    public void logout() {
        authenticationService.logout();
    }

    // Trainer operations

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    public Optional<Trainer> selectTrainer(Long id) {
        return trainerService.selectTrainer(id);
    }

    public Optional<Trainer> selectTrainerByUsername(String name) {
        return trainerService.selectTrainerByUsername(name);
    }

    public void toggleTrainerStatus(Long id) {
        trainerService.toggleTrainerStatus(id);
    }

    public Trainer changeTrainerPassword(Long id, String newPassword) {
        return trainerService.changeTrainerPassword(id, newPassword);
    }

    public List<Training> getTrainerTrainings(String trainerUsername, Date from, Date to, String traineeUsername) {
        return trainerService.getTrainerTrainings(trainerUsername, from, to, traineeUsername);
    }

    public List<Trainer> selectAllTrainers() {
        return trainerService.selectAllTrainers();
    }

    public void updateTrainer(Trainer trainer) {
        trainerService.updateTrainer(trainer);
    }

    public List<Trainer> getTrainerNotAssignedToTrainee(String username) {
        return trainerService.getTrainersNotAssignedToTrainee(username);
    }

    // Trainee operations

    public Trainee createTrainee(Trainee trainee) {
        return traineeService.createTrainee(trainee);
    }

    public Optional<Trainee> selectTrainee(Long id) {
        return traineeService.selectTrainee(id);
    }

    public Optional<Trainee> selectTraineeByUsername(String name) {
        return traineeService.selectTraineeByUsername(name);
    }

    public List<Trainee> selectAllTrainees() {
        return traineeService.selectAllTrainees();
    }

    public void updateTrainee(Trainee trainee) {
        traineeService.updateTrainee(trainee);
    }

    public Trainee changeTraineePassword(Long id, String newPassword) {
        return traineeService.changeTraineePassword(id, newPassword);
    }

    public void toggleTraineeStatus(Long id) {
        traineeService.toggleTraineeStatus(id);
    }

    public void deleteTrainee(String username) {
        traineeService.deleteTrainee(username);
    }

    public List<Training> getTraineeTrainings(String traineeUsername, Date from, Date to, String trainerUsername, TrainingType trainingType) {
        return traineeService.getTraineeTrainings(traineeUsername, from, to, trainerUsername, trainingType);
    }

    // Training operations

    public Training createTraining(Training training) {
        return trainingService.createTraining(training);
    }

    // Training Type operations

    public Optional<TrainingType> getTrainingTypeById(long id) {
        return trainingTypeService.getTrainingTypeById(id);
    }
}
