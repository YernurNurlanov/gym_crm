package com.crm.gym.dto;

public class TrainersListDTO {

    private String username;
    private String fName;
    private String lName;
    private long specialization;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public void setSpecialization(long specialization) {
        this.specialization = specialization;
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

    public long getSpecialization() {
        return specialization;
    }
}
