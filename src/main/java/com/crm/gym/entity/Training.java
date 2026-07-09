package com.crm.gym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @Column(nullable = false)
    @NotEmpty(message = "Training name must not be empty")
    private String name;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Integer duration;

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + id +
                ", trainerId=" + trainer.getUserId() +
                ", traineeId=" + trainee.getUserId() +
                ", trainingName='" + name + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + date +
                ", trainingDuration=" + duration +
                '}';
    }
}
