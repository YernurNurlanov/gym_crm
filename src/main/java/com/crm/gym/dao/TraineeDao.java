package com.crm.gym.dao;

import com.crm.gym.entity.Trainee;
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
public class TraineeDao {

    private final Map<Long, Trainee> traineeStorage;

    private CredentialGenerator credentialGenerator;

    private final AtomicLong idGenerator = new AtomicLong(0);

    private static final Logger logger =
            LoggerFactory.getLogger(TraineeDao.class);

    @Autowired
    public void setCredentialGenerator(CredentialGenerator credentialGenerator) {
        this.credentialGenerator = credentialGenerator;
    }

    public TraineeDao(
            @Qualifier("traineeStorage")
            Map<Long, Trainee> traineeStorage) {

        this.traineeStorage = traineeStorage;
    }

    public Trainee create(Trainee trainee) {

        trainee.setUserId(idGenerator.incrementAndGet());
        trainee.setUsername(credentialGenerator.generateUniqueUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(credentialGenerator.generatePassword());

        traineeStorage.put(trainee.getUserId(), trainee);

        return trainee;
    }

    public void update(Trainee trainee) {
        traineeStorage.put(trainee.getUserId(), trainee);
    }

    public Trainee select(Long id) {

        Trainee trainee = traineeStorage.get(id);

        if (trainee == null) {
            logger.warn(
                    "Trainee not found for id={}",
                    id);
        }

        return trainee;
    }

    public void delete(Long id) {
        if (traineeStorage.containsKey(id)) {
            traineeStorage.remove(id);
            logger.info(
                    "Trainee deleted for id={}",
                    id);
        } else {
            logger.warn(
                    "Trainee not found for deletion for id={}",
                    id);
        }
    }

    public List<Trainee> selectAll() {

        logger.debug(
                "Selecting all trainees, count={}",
                traineeStorage.size());

        return new ArrayList<>(traineeStorage.values());
    }

    public void setMaxId(Long maxId) {
        this.idGenerator.set(maxId);
    }
}
