package com.crm.gym.service;

import com.crm.gym.entity.TrainingType;
import com.crm.gym.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public Optional<TrainingType> getTrainingTypeById(long id) {
        return trainingTypeRepository.findById(id);
    }
}
