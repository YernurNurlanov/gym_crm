package com.trainings.trainer.dto;

import jakarta.persistence.*;

import java.time.Month;

public class MonthSummary {

    @Enumerated(EnumType.STRING)
    private Month workloadMonth;

    private Integer duration;

    public MonthSummary() {}

    public MonthSummary(Month workloadMonth, Integer duration) {
        this.workloadMonth = workloadMonth;
        this.duration = duration;
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
}
