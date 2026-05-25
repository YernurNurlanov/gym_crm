package com.crm.gym.repository;

import com.crm.gym.entity.Trainee;
import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TraineeRepository {

    @PersistenceContext
    private final EntityManager em;

    public TraineeRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public Trainee save(Trainee trainee) {

        if (trainee.getUserId() == null) {
            em.persist(trainee);
        } else {
            em.merge(trainee);
        }
        return trainee;

    }

    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(
                em.find(Trainee.class, id));
    }

    public Optional<Trainee> findByUsername(String username) {

        String jpql = """
                SELECT t
                FROM Trainee t
                WHERE t.username = :username
                """;

        List<Trainee> result = em
                .createQuery(jpql, Trainee.class)
                .setParameter("username", username)
                .getResultList();

        return result.stream().findFirst();
    }

    @Transactional
    public void delete(Trainee trainee) {
        em.remove(trainee);
    }

    public List<Trainee> findAll() {

        return em
                .createQuery(
                        "SELECT t FROM Trainee t",
                        Trainee.class)
                .getResultList();
    }

    public List<Training> getTrainings(Trainee trainee, Date from, Date to, String trainerUsername, TrainingType trainingType) {
        return trainee.getTrainings().stream()
                .filter(training -> Objects.equals(training.getTrainer().getUsername(), trainerUsername))
                .filter(training -> training.getTrainingType().equals(trainingType))
                .filter(training -> from == null ||
                        !training.getTrainingDate().before(from))
                .filter(training -> to == null ||
                        !training.getTrainingDate().after(to))
                .collect(Collectors.toList());
    }

}
