package com.crm.gym.repository;

import com.crm.gym.entity.Trainer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository
        extends CrudRepository<Trainer, Long> {

    Optional<Trainer> findByUsername(
            String username);

    List<Trainer> findByUsernameIn(
            List<String> usernames);

    @Query("""
        SELECT t
        FROM Trainer t
        WHERE t.isActive = true
        AND t NOT IN (
            SELECT tr
            FROM Trainee tn
            JOIN tn.trainers tr
            WHERE tn.username = :username
        )
    """)
    List<Trainer> findActiveTrainersNotAssignedToTrainee(
            @Param("username") String username);

    @Override
    List<Trainer> findAll();
}