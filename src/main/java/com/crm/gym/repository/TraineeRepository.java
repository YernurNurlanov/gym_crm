package com.crm.gym.repository;

import com.crm.gym.entity.Trainee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository
        extends CrudRepository<Trainee, Long> {

    Optional<Trainee> findByUsername(
            String username);

    List<Trainee> findAll();
}
