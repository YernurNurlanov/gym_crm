package com.crm.gym.dao;

import com.crm.gym.entity.Trainer;
import com.crm.gym.util.CredentialGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainerDao {

    private final Map<Long, Trainer> trainerStorage;

    private CredentialGenerator credentialGenerator;

    private final AtomicLong idGenerator = new AtomicLong(0);

    private static final Logger logger =
            LoggerFactory.getLogger(TrainerDao.class);

    public TrainerDao(
            @Qualifier("trainerStorage")
            Map<Long, Trainer> trainerStorage) {

        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setCredentialGenerator(CredentialGenerator credentialGenerator) {
        this.credentialGenerator = credentialGenerator;
    }

    public Trainer create(Trainer trainer) {

        trainer.setUserId(idGenerator.incrementAndGet());
        trainer.setUsername(credentialGenerator.generateUniqueUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(credentialGenerator.generatePassword());

        trainerStorage.put(trainer.getUserId(), trainer);

        return trainer;
    }

    public void update(Trainer trainer) {
        trainerStorage.put(trainer.getUserId(), trainer);
    }

    public Trainer select(Long id) {

        Trainer trainer = trainerStorage.get(id);

        if (trainer == null) {
            logger.warn(
                    "Trainer not found for id={}",
                    id);
        }

        return trainer;
    }

    public List<Trainer> selectAll() {

        logger.debug(
                "Selecting all trainers, count={}",
                trainerStorage.size());

        return new ArrayList<>(trainerStorage.values());
    }

    public void setMaxId(long maxId) {
        this.idGenerator.set(maxId);
    }
}