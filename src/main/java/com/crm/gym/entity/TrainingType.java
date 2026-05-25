package com.crm.gym.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "training_types")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long trainingTypeId;

    @Column(nullable = false)
    private String trainingTypeName;

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public Long getTrainingTypeId() {
        return trainingTypeId;
    }
}
