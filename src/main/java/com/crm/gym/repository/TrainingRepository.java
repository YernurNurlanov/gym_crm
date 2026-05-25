package com.crm.gym.repository;

import com.crm.gym.entity.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingRepository {

    @PersistenceContext
    private final EntityManager em;

    public TrainingRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public Training save(Training training) {
        em.persist(training);
        return training;
    }

}
