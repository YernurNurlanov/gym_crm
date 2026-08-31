package com.trainings.trainer.service;

import com.trainings.trainer.dto.YearSummary;
import com.trainings.trainer.dto.TrainerWorkloadRequest;
import com.trainings.trainer.entity.ActionType;
import com.trainings.trainer.dto.MonthSummary;
import com.trainings.trainer.entity.TrainerWorkload;
import com.trainings.trainer.repository.TrainerWorkloadRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

@Service
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;

    public TrainerWorkloadService(TrainerWorkloadRepository repository) {
        this.repository = repository;
    }

    public void updateWorkload(TrainerWorkloadRequest request) {

        TrainerWorkload trainer =
                repository.findByUsername(request.getUsername())
                        .orElseGet(() -> createTrainer(request));

        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setActive(request.isActive());

        LocalDate localDate = request.getTrainingDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        int year = localDate.getYear();
        Month month = localDate.getMonth();

        YearSummary yearSummary = findYearSummary(trainer, year);

        if (yearSummary == null) {
            yearSummary = createYearSummary(year);
            trainer.getYears().add(yearSummary);
        }

        MonthSummary monthSummary = findMonthSummary(yearSummary, month);

        if (monthSummary == null) {
            monthSummary = createMonthSummary(month);
            yearSummary.getMonths().add(monthSummary);
        }

        updateDuration(monthSummary, request);

        repository.save(trainer);
    }

    private TrainerWorkload createTrainer(TrainerWorkloadRequest request) {

        TrainerWorkload trainer = new TrainerWorkload();

        trainer.setUsername(request.getUsername());
        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setActive(request.isActive());

        return trainer;
    }

    private YearSummary findYearSummary(TrainerWorkload trainer, int year) {

        return trainer.getYears()
                .stream()
                .filter(y -> y.getCalendarYear() == year)
                .findFirst()
                .orElse(null);
    }

    private MonthSummary findMonthSummary(YearSummary yearSummary, Month month) {

        return yearSummary.getMonths()
                .stream()
                .filter(m -> m.getWorkloadMonth() == month)
                .findFirst()
                .orElse(null);
    }

    private YearSummary createYearSummary(int year) {

        YearSummary summary = new YearSummary();

        summary.setCalendarYear(year);

        return summary;
    }

    private MonthSummary createMonthSummary(Month month) {

        MonthSummary summary = new MonthSummary();

        summary.setWorkloadMonth(month);
        summary.setDuration(0);

        return summary;
    }

    private void updateDuration(MonthSummary summary, TrainerWorkloadRequest request) {

        if (request.getActionType() == ActionType.ADD) {

            summary.setDuration(
                    summary.getDuration()
                            + request.getTrainingDuration());

            return;
        }

        int result =
                summary.getDuration()
                        - request.getTrainingDuration();

        if (result < 0) {
            throw new IllegalArgumentException(
                    "Monthly workload cannot be negative.");
        }

        summary.setDuration(result);
    }
}
