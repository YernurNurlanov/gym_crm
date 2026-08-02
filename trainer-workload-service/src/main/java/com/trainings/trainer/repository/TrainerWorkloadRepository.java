package com.trainings.trainer.repository;

import com.trainings.trainer.entity.TrainerWorkload;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerWorkloadRepository extends CrudRepository<TrainerWorkload, Long> {

    Optional<TrainerWorkload> findByUsername(String username);

}
