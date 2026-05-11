package com.crm.gym.dao;

import com.crm.gym.entity.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainingDao {

    private final Map<Long, Training> trainingStorage;

    private final AtomicLong idGenerator = new AtomicLong(0);

    private static final Logger logger =
            LoggerFactory.getLogger(TrainingDao.class);

    public TrainingDao(
            @Qualifier("trainingStorage")
            Map<Long, Training> trainingStorage) {

        this.trainingStorage = trainingStorage;
    }

    public Training create(Training training) {

        training.setTrainingId(idGenerator.incrementAndGet());

        trainingStorage.put(training.getTrainingId(), training);

        logger.debug(
                "Training stored in memory with id={}",
                training.getTrainingId());

        return training;
    }

    public Training select(Long id) {
        Training training = trainingStorage.get(id);

        if (training == null) {
            logger.warn(
                    "Training not found for id={}",
                    id);
        }

        return training;
    }

    public List<Training> selectAll() {

        logger.debug(
                "Selecting all trainings, count={}",
                trainingStorage.size());

        return new ArrayList<>(trainingStorage.values());
    }

    public void setMaxId(Long maxId) {
        this.idGenerator.set(maxId);
    }
}
