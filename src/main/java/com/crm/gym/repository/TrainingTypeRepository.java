package com.crm.gym.repository;

import com.crm.gym.entity.TrainingType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository
        extends CrudRepository<TrainingType, Long> {
}
