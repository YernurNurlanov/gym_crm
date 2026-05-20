package com.crm.gym.service;

import com.crm.gym.dao.TrainerDao;
import com.crm.gym.entity.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    @Autowired
    private TrainerDao trainerDao;

    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(TrainerService.class);

    public Trainer createTrainer(Trainer trainer) {

        logger.info("Creating trainer: {} {}",
                trainer.getFirstName(),
                trainer.getLastName());

        trainer = trainerDao.create(trainer);

        logger.info("Trainer created with id={}",
                trainer.getUserId());

        return trainer;
    }

    public void updateTrainer(Trainer trainer) {

        Trainer existing =
                trainerDao.select(trainer.getUserId());

        if (existing == null) {
            throw new RuntimeException("Trainer not found");
        }

        trainerDao.update(trainer);
    }

    public Trainer selectTrainer(Long id) {
        return trainerDao.select(id);
    }

    public List<Trainer> selectAllTrainers() {
        return trainerDao.selectAll();
    }

}
