package com.trainings.trainer.entity;

import com.trainings.trainer.dto.YearSummary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("trainer_workloads")
@CompoundIndex(
        name = "trainer_name_idx",
        def = "{'firstName':1,'lastName':1}"
)
public class TrainerWorkload {

    @Id
    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private boolean active;

    private List<YearSummary> years = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<YearSummary> getYears() {
        return years;
    }

    public void setYears(List<YearSummary> years) {
        this.years = years;
    }
}