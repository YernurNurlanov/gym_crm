package com.crm.gym.service;

import com.crm.gym.dto.*;
import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import com.crm.gym.exception.NotFoundException;
import com.crm.gym.repository.TraineeRepository;
import com.crm.gym.repository.TrainerRepository;
import com.crm.gym.repository.TrainingRepository;
import com.crm.gym.repository.TrainingTypeRepository;
import com.crm.gym.util.CredentialGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final CredentialGenerator credentialGenerator;
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainerService(TrainerRepository trainerRepository, CredentialGenerator credentialGenerator, TraineeRepository traineeRepository, TrainingRepository trainingRepository, TrainingTypeRepository trainingTypeRepository) {
        this.trainerRepository = trainerRepository;
        this.credentialGenerator = credentialGenerator;
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional
    public RegistrationResponse createTrainer(TrainerRegistrationRequest request) {

        Trainer trainer = new Trainer();

        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());

        Optional<TrainingType> type = trainingTypeRepository.findById(request.getSpecializationId());
        if (type.isPresent()) {
            trainer.setSpecialization(type.get());
        } else {
            throw new NotFoundException("Specialization with id " + request.getSpecializationId() + " not found");
        }

        trainer.setUsername(credentialGenerator.generateUniqueUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(credentialGenerator.generatePassword());

        trainer = trainerRepository.save(trainer);

        RegistrationResponse response = new RegistrationResponse();
        response.setUsername(trainer.getUsername());
        response.setPassword(trainer.getPassword());

        return response;
    }

    public TrainerDTO getTrainer(String username) {
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);

        if (trainer.isPresent()) {
            TrainerDTO trainerDTO = new TrainerDTO();

            ArrayList<TraineesListDTO> trainees =
                    trainer.get().getTrainees()
                            .stream()
                            .map(this::mapTraineeToDto)
                            .collect(Collectors.toCollection(
                                    ArrayList::new));

            trainerDTO.setFName(trainer.get().getFirstName());
            trainerDTO.setLName(trainer.get().getLastName());
            if (trainer.get().getSpecialization() != null) {
                trainerDTO.setSpecialization(trainer.get().getSpecialization().getId());
            }
            trainerDTO.setActive(trainer.get().isActive());
            trainerDTO.setTrainees(trainees);

            return trainerDTO;
        } else {
            throw new NotFoundException("Trainer with username " + username + " not found");
        }
    }

    @Transactional
    public UpdateTrainerResponse updateTrainer(UpdateTrainerRequest request) {

        Optional<Trainer> trainer = trainerRepository.findByUsername(request.getUsername());

        if (trainer.isEmpty()) {
            throw new NotFoundException("Trainer with username " + request.getUsername() + " not found");
        }

        Trainer trainerEntity = trainer.get();

        trainerEntity.setFirstName(request.getFName());
        trainerEntity.setLastName(request.getLName());
        if (request.getSpecializationId() != null) {
            if (trainingTypeRepository.findById(request.getSpecializationId()).isPresent()) {
                trainerEntity.setSpecialization(trainingTypeRepository.findById(request.getSpecializationId()).get());
            } else {
                throw new NotFoundException("Specialization with id " + request.getSpecializationId() + " not found");
            }
        }
        trainerEntity.setActive(request.isActive());

        trainerEntity = trainerRepository.save(trainerEntity);

        UpdateTrainerResponse response = new UpdateTrainerResponse();

        ArrayList<TraineesListDTO> trainees =
                trainer.get().getTrainees()
                        .stream()
                        .map(this::mapTraineeToDto)
                        .collect(Collectors.toCollection(
                                ArrayList::new));

        response.setUsername(trainerEntity.getUsername());
        response.setFName(trainerEntity.getFirstName());
        response.setLName(trainerEntity.getLastName());
        if (trainerEntity.getSpecialization() != null) {
            response.setSpecialization(trainerEntity.getSpecialization().getId());
        }
        response.setActive(trainerEntity.isActive());
        response.setTrainees(trainees);

        return response;
    }

    public List<TrainersListDTO> getTrainersNotAssignedToTrainee(String username) {

        if (traineeRepository.findByUsername(username).isPresent()) {

            List<Trainer> trainers = trainerRepository.findActiveTrainersNotAssignedToTrainee(username);

            return trainers.stream()
                    .map(this::mapTrainerToDto)
                    .collect(Collectors.toCollection(
                            ArrayList::new));

        } else {
            throw new NotFoundException("Trainee with username " + username + " not found");
        }
    }

    @Transactional
    public List<TrainersListDTO> updateTraineesTrainersList(UpdateTraineesTrainersListRequest request) {

        Optional<Trainee> trainee = traineeRepository.findByUsername(request.getTraineeUsername());

        if (trainee.isEmpty()) {
            throw new NotFoundException("Trainee with username " + request.getTraineeUsername() + " not found");
        }

        List<Trainer> trainers =
                trainerRepository.findByUsernameIn(request.getTrainers());

        if (trainers.size() != request.getTrainers().size()) {
            throw new NotFoundException(
                    "One or more trainers do not exist");
        }

        trainee.get().setTrainers(trainers);
        Trainee traineeEntity = traineeRepository.save(trainee.get());

        return traineeEntity.getTrainers()
                .stream()
                .map(this::mapTrainerToDto)
                .collect(Collectors.toCollection(
                        ArrayList::new));
    }

    public List<TrainingDTO> getTrainerTrainings(TrainerTrainingsRequest request) {

        List<Training> trainings =
                trainingRepository.findTrainerTrainings(
                        request.getUsername(),
                        request.getPeriodFrom(),
                        request.getPeriodTo(),
                        request.getTraineeName());

        return trainings.stream()
                .map(this::mapTrainingToDto)
                .toList();
    }

    @Transactional
    public ResponseEntity<Void> setTrainerStatus(UserIsActiveRequest request) {

        Optional<Trainer> trainer = trainerRepository.findByUsername(request.getUsername());
        if (trainer.isPresent()) {
            Trainer trainerEntity = trainer.get();
            trainerEntity.setActive(request.isActive());
            trainerRepository.save(trainerEntity);

            return ResponseEntity.ok().build();

        } else {
            throw new NotFoundException("Trainer with username " + request.getUsername() + " not found");
        }
    }

    private TraineesListDTO mapTraineeToDto(Trainee trainee) {

        TraineesListDTO dto =
                new TraineesListDTO();

        dto.setUsername(trainee.getUsername());
        dto.setFName(trainee.getFirstName());
        dto.setLName(trainee.getLastName());

        return dto;
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
        dto.setUsername(training.getTrainee().getUsername());

        return dto;
    }
}
