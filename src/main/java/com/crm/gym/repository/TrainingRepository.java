package com.crm.gym.repository;

import com.crm.gym.entity.Training;
import com.crm.gym.entity.TrainingType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository
        extends CrudRepository<Training, Long> {

    @Override
    List<Training> findAll();

    @Query("""
        SELECT t
        FROM Training t
        WHERE t.trainee.username = :username
        AND (:from IS NULL OR t.trainingDate >= :from)
        AND (:to IS NULL OR t.trainingDate <= :to)
        AND (:trainerUsername IS NULL
             OR t.trainer.username = :trainerUsername)
        AND (:trainingType IS NULL
             OR t.trainingType = :trainingType)
    """)
    List<Training> findTraineeTrainings(
            @Param("username") String username,
            @Param("from") Date from,
            @Param("to") Date to,
            @Param("trainerUsername") String trainerUsername,
            @Param("trainingType") TrainingType trainingType);

    @Query("""
        SELECT t
        FROM Training t
        WHERE t.trainer.username = :username
        AND (:from IS NULL OR t.trainingDate >= :from)
        AND (:to IS NULL OR t.trainingDate <= :to)
        AND (:traineeUsername IS NULL
             OR t.trainee.username = :traineeUsername)
    """)
    List<Training> findTrainerTrainings(
            @Param("username") String username,
            @Param("from") Date from,
            @Param("to") Date to,
            @Param("traineeUsername") String traineeUsername);
}
