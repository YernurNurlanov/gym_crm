package com.crm.gym.service;

import com.crm.gym.dao.TrainingDao;
import com.crm.gym.entity.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {

    @Autowired
    private TrainingDao trainingDao;

    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(TrainingService.class);

    public Training createTraining(Training training) {
        logger.info(
                "Creating training '{}' for trainee={} with trainer={}",
                training.getTrainingName(),
                training.getTraineeId(),
                training.getTrainerId());

        training = trainingDao.create(training);

        logger.info("Training created with id={}",
                training.getTrainingId());

        return training;
    }

    public Training selectTraining(Long id) {
        return trainingDao.select(id);
    }

    public List<Training> selectAllTrainings() {
        return trainingDao.selectAll();
    }

}
