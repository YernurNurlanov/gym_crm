package com.crm.gym.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "trainees")
public class Trainee extends User {

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column
    private String address;

    @ManyToMany
    @JoinTable(
            name = "trainee_trainers",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<Trainer> trainers = new ArrayList<>();

    @OneToMany(
            mappedBy = "trainee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Training> trainings = new ArrayList<>();

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    @Override
    public String toString() {
        return "Trainee{" + super.toString() +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                '}';
    }
}