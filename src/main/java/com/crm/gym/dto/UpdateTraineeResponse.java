package com.crm.gym.dto;

import java.util.ArrayList;
import java.util.Date;

public class UpdateTraineeResponse {

    private String username;
    private String fName;
    private String lName;
    private Date dateOfBirth;
    private String address;
    private boolean active;
    private ArrayList<TrainersListDTO> trainers;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTrainers(ArrayList<TrainersListDTO> trainers) {
        this.trainers = trainers;
    }

    public String getUsername() {
        return username;
    }

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public boolean isActive() {
        return active;
    }

    public ArrayList<TrainersListDTO> getTrainers() {
        return trainers;
    }
}
