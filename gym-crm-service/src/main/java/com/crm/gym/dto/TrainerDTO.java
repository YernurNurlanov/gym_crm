package com.crm.gym.dto;

import java.util.ArrayList;

public class TrainerDTO {

    private String fName;
    private String lName;
    private Long specialization;
    private boolean active;
    private ArrayList<TraineesListDTO> trainees;

    public void setFName(String fName) {
        this.fName = fName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public void setSpecialization(Long specialization) {
        this.specialization = specialization;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTrainees(ArrayList<TraineesListDTO> trainees) {
        this.trainees = trainees;
    }

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }

    public Long getSpecialization() {
        return specialization;
    }

    public boolean isActive() {
        return active;
    }

    public ArrayList<TraineesListDTO> getTrainees() {
        return trainees;
    }
}
