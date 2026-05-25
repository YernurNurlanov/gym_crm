package com.crm.gym.service;

import com.crm.gym.entity.Training;
import com.crm.gym.repository.TrainingRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(TrainingService.class);

    public Training createTraining(@Valid Training training) {
        logger.info(
                "Creating training '{}' for with trainer={}",
                training.getTrainingName(),
                training.getTrainer().getUserId());

        training = trainingRepository.save(training);

        logger.info("Training created with id={}",
                training.getTrainingId());

        return training;
    }

}
