package com.trainings.trainer.dto;

import java.util.ArrayList;
import java.util.List;

public class YearSummary {

    private Integer calendarYear;

    private List<MonthSummary> months = new ArrayList<>();

    public Integer getCalendarYear() {
        return calendarYear;
    }

    public void setCalendarYear(Integer calendarYear) {
        this.calendarYear = calendarYear;
    }

    public List<MonthSummary> getMonths() {
        return months;
    }

    public void setMonths(List<MonthSummary> months) {
        this.months = months;
    }
}
