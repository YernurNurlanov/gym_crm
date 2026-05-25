package com.crm.gym.repository;

import com.crm.gym.entity.Trainer;
import com.crm.gym.entity.Training;
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
public class TrainerRepository {

    @PersistenceContext
    private final EntityManager em;

    public TrainerRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public Trainer save(Trainer trainer) {

        if (trainer.getUserId() == null) {
            em.persist(trainer);
        } else {
            em.merge(trainer);
        }
        return trainer;

    }

    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(
                em.find(Trainer.class, id));
    }

    public Optional<Trainer> findByUsername(String username) {

        String jpql = """
                SELECT t
                FROM Trainer t
                WHERE t.username = :username
                """;

        List<Trainer> result = em
                .createQuery(jpql, Trainer.class)
                .setParameter("username", username)
                .getResultList();

        return result.stream().findFirst();
    }

    public List<Trainer> findAll() {

        return em
                .createQuery(
                        "SELECT t FROM Trainer t",
                        Trainer.class)
                .getResultList();
    }

    @Transactional
    public void delete(Trainer trainer) {
        em.remove(trainer);
    }

    public List<Training> getTrainings(Trainer trainer, Date from, Date to, String traineeUsername) {
        return trainer.getTrainings().stream()
                .filter(training -> Objects.equals(training.getTrainee().getUsername(), traineeUsername))
                .filter(training -> from == null ||
                        !training.getTrainingDate().before(from))
                .filter(training -> to == null ||
                        !training.getTrainingDate().after(to))
                .collect(Collectors.toList());
    }

    public List<Trainer> getTrainersNotAssignedToTrainee(String username) {
        String hql = "SELECT t FROM Trainer t " +
                "WHERE t NOT IN (" +
                "  SELECT tr FROM Trainee tn JOIN tn.trainers tr WHERE tn.username = :username" +
                ")";

        return em.createQuery(hql, Trainer.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Trainer> findByUsernames(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return List.of();
        }
        return em.createQuery(
                        "SELECT t FROM Trainer t WHERE t.username IN :usernames", Trainer.class)
                .setParameter("usernames", usernames)
                .getResultList();
    }
}
