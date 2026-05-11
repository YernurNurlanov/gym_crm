package com.crm.gym.service;

import com.crm.gym.dao.TraineeDao;
import com.crm.gym.entity.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {

    @Autowired
    private TraineeDao traineeDao;

    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(TraineeService.class);

    public Trainee createTrainee(Trainee trainee) {

        logger.info("Creating trainee: {} {}",
                trainee.getFirstName(),
                trainee.getLastName());

        trainee = traineeDao.create(trainee);

        logger.info("Trainee created with id={}",
                trainee.getUserId());

        return trainee;
    }

    public void updateTrainee(Trainee trainee) {

        Trainee existing =
                traineeDao.select(trainee.getUserId());

        if (existing == null) {
            throw new RuntimeException("Trainee not found");
        }

        traineeDao.update(trainee);
    }

    public Trainee selectTrainee(Long id) {
        return traineeDao.select(id);
    }

    public void deleteTrainee(Long id) {

        Trainee existing = traineeDao.select(id);

        if (existing == null) {
            throw new RuntimeException("Trainee not found");
        }

        traineeDao.delete(id);
    }

    public List<Trainee> selectAllTrainees() {
        return traineeDao.selectAll();
    }

}
