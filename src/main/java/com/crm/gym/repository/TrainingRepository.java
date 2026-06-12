package com.crm.gym.repository;

import com.crm.gym.entity.Training;
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
    AND (:from IS NULL OR t.date >= :from)
    AND (:to IS NULL OR t.date <= :to)
    AND (:trainerUsername IS NULL
         OR t.trainer.username = :trainerUsername)
    AND (:trainingTypeId IS NULL
         OR t.trainingType.id = :trainingTypeId)
""")
    List<Training> findTraineeTrainings(
            @Param("username") String username,
            @Param("from") Date from,
            @Param("to") Date to,
            @Param("trainerUsername") String trainerUsername,
            @Param("trainingTypeId") Long trainingTypeId
    );

    @Query("""
        SELECT t
        FROM Training t
        WHERE t.trainer.username = :username
        AND (:from IS NULL OR t.date >= :from)
        AND (:to IS NULL OR t.date <= :to)
        AND (:traineeUsername IS NULL
             OR t.trainee.username = :traineeUsername)
    """)
    List<Training> findTrainerTrainings(
            @Param("username") String username,
            @Param("from") Date from,
            @Param("to") Date to,
            @Param("traineeUsername") String traineeUsername);
}
