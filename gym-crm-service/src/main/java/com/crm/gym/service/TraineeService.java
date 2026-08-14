package com.crm.gym.service;

import com.crm.gym.dto.*;
import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.exception.NotFoundException;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.repository.TrainingRepository;
import com.crm.gym.util.CredentialGenerator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final CredentialGenerator credentialGenerator;
    private final TrainingRepository trainingRepository;
    private final PasswordEncoder passwordEncoder;

    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository, CredentialGenerator credentialGenerator, TrainingRepository trainingRepository, PasswordEncoder passwordEncoder) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.credentialGenerator = credentialGenerator;
        this.trainingRepository = trainingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegistrationResponse createTrainee(@Valid TraineeRegistrationRequest request) {

        Trainee trainee = new Trainee();

        trainee.setFirstName(request.getFirstName());
        trainee.setLastName(request.getLastName());
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.setAddress(request.getAddress());

        trainee.setUsername(credentialGenerator.generateUniqueUsername(trainee.getFirstName(), trainee.getLastName()));

        String password = credentialGenerator.generatePassword();
        trainee.setPassword(passwordEncoder.encode(password));

        trainee = traineeRepository.save(trainee);

        RegistrationResponse response = new RegistrationResponse();
        response.setUsername(trainee.getUsername());
        response.setPassword(password);

        return response;
    }

    public TraineeDTO getTrainee(String username) {
        Optional<Trainee> trainee = traineeRepository.findByUsername(username);

        if (trainee.isPresent()) {
            ArrayList<TrainersListDTO> trainers =
                    trainee.get().getTrainers()
                            .stream()
                            .map(this::mapTrainerToDto)
                            .collect(Collectors.toCollection(
                                    ArrayList::new));

            TraineeDTO traineeDTO = new TraineeDTO();
            traineeDTO.setFName(trainee.get().getFirstName());
            traineeDTO.setLName(trainee.get().getLastName());
            traineeDTO.setDateOfBirth(trainee.get().getDateOfBirth());
            traineeDTO.setAddress(trainee.get().getAddress());
            traineeDTO.setActive(trainee.get().isActive());
            traineeDTO.setTrainers(trainers);

            return traineeDTO;
        } else {
            throw new NotFoundException("Trainee with username " + username + " not found");
        }
    }

    @Transactional
    public UpdateTraineeResponse updateTrainee(UpdateTraineeRequest request) {

        Optional<Trainee> trainee = traineeRepository.findByUsername(request.getUsername());

        if (trainee.isEmpty()) {
            throw new NotFoundException("Trainee with username " + request.getUsername() + " not found");
        }

        Trainee traineeEntity = trainee.get();

        traineeEntity.setFirstName(request.getFName());
        traineeEntity.setLastName(request.getlName());
        if (request.getDateOfBirth() != null) { traineeEntity.setDateOfBirth(request.getDateOfBirth()); }
        if (request.getAddress() != null) { traineeEntity.setAddress(request.getAddress()); }
        traineeEntity.setActive(request.isActive());

        traineeEntity = traineeRepository.save(traineeEntity);

        UpdateTraineeResponse response = new UpdateTraineeResponse();

        ArrayList<TrainersListDTO> trainers =
                trainee.get().getTrainers()
                        .stream()
                        .map(this::mapTrainerToDto)
                        .collect(Collectors.toCollection(
                                ArrayList::new));

        response.setUsername(traineeEntity.getUsername());
        response.setFName(traineeEntity.getFirstName());
        response.setLName(traineeEntity.getLastName());
        response.setDateOfBirth(traineeEntity.getDateOfBirth());
        response.setAddress(traineeEntity.getAddress());
        response.setActive(traineeEntity.isActive());
        response.setTrainers(trainers);

        return response;
    }

    @Transactional
    public ResponseEntity<Void> deleteTrainee(String username) {
        Optional<Trainee> trainee = traineeRepository.findByUsername(username);

        if (trainee.isEmpty()) {
            throw new NotFoundException("Trainee with username " + username + " not found");
        }

        traineeRepository.delete(trainee.get());

        return ResponseEntity.ok().build();
    }

    public List<TrainingDTO> getTraineeTrainings(TraineeTrainingsRequest request) {

        Optional<Trainee> trainee = traineeRepository.findByUsername(request.getUsername());
        if (trainee.isEmpty()) {
            throw new NotFoundException("Trainee with username " + request.getUsername() + " not found");
        }

        Optional<Trainer> trainer = trainerRepository.findByUsername(request.getTrainerName());
        if (trainer.isEmpty()) {
            throw new NotFoundException("Trainer with username " + request.getTrainerName() + " not found");
        }

        List<Training> trainings = trainingRepository.findTraineeTrainings(trainee.get().getUsername(), request.getPeriodFrom(), request.getPeriodTo(), request.getUsername(), request.getTrainingTypeId());

        return trainings.stream()
                .map(this::mapTrainingToDto)
                .collect(Collectors.toCollection(
                        ArrayList::new));
    }

    @Transactional
    public ResponseEntity<Void> setTraineeStatus(UserIsActiveRequest request) {

        Optional<Trainee> trainee = traineeRepository.findByUsername(request.getUsername());
        if (trainee.isPresent()) {
            trainee.get().setActive(request.isActive());
            traineeRepository.save(trainee.get());

            return ResponseEntity.ok().build();

        } else {
            throw new NotFoundException("Trainee with username " + request.getUsername() + " not found");
        }

    }

    private TrainersListDTO mapTrainerToDto(Trainer trainer) {

        TrainersListDTO dto = new TrainersListDTO();

        dto.setUsername(trainer.getUsername());
        dto.setFName(trainer.getFirstName());
        dto.setLName(trainer.getLastName());
        if (trainer.getSpecialization() != null) {
            dto.setSpecialization(trainer.getSpecialization().getId());
        }


        return dto;
    }

    private TrainingDTO mapTrainingToDto(Training training) {

        TrainingDTO dto = new TrainingDTO();

        dto.setTrainingName(training.getName());
        dto.setTrainingDate(training.getDate());
        dto.setTrainingType(training.getTrainingType().getName());
        dto.setTrainingDuration(training.getDuration());
        dto.setUsername(training.getTrainer().getUsername());

        return dto;
    }
}
