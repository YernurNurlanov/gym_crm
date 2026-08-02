package com.trainings.trainer.entity;

import jakarta.persistence.*;

import java.time.Month;

@Entity
public class MonthSummary {

    @Id
    @GeneratedValue
    private int id;

    private Integer calendarYear;

    @Enumerated(EnumType.STRING)
    private Month workloadMonth;

    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerWorkload trainer;

    public MonthSummary() {}

    public MonthSummary(Integer calendarYear, Month workloadMonth, Integer duration, TrainerWorkload trainer) {
        this.calendarYear = calendarYear;
        this.workloadMonth = workloadMonth;
        this.duration = duration;
        this.trainer = trainer;
    }

    public int getId() {
        return id;
    }

    public Integer getCalendarYear() {
        return calendarYear;
    }

    public void setCalendarYear(Integer calendarYear) {
        this.calendarYear = calendarYear;
    }

    public Month getWorkloadMonth() {
        return workloadMonth;
    }

    public void setWorkloadMonth(Month workloadMonth) {
        this.workloadMonth = workloadMonth;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public TrainerWorkload getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerWorkload trainer) {
        this.trainer = trainer;
    }
}
