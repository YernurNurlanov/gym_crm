package com.trainings.trainer.service;

import com.trainings.trainer.dto.request.TrainerWorkloadRequest;
import com.trainings.trainer.entity.ActionType;
import com.trainings.trainer.entity.MonthSummary;
import com.trainings.trainer.entity.TrainerWorkload;
import com.trainings.trainer.repository.TrainerWorkloadRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Month;

@Service
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;

    public TrainerWorkloadService(TrainerWorkloadRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void updateWorkload(TrainerWorkloadRequest request) {

        TrainerWorkload trainer =
                repository.findByUsername(request.getUsername())
                        .orElseGet(() -> createTrainer(request));

        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setActive(request.isActive());

        int year = request.getTrainingDate().getYear();
        Month month = Month.of(request.getTrainingDate().getMonth());

        MonthSummary summary = findMonthSummary(trainer, year, month);

        if (summary == null) {

            summary = new MonthSummary();

            summary.setTrainer(trainer);
            summary.setCalendarYear(year);
            summary.setWorkloadMonth(month);
            summary.setDuration(0);

            trainer.getSummaries().add(summary);
        }

        if (request.getActionType() == ActionType.ADD) {

            summary.setDuration(
                    summary.getDuration()
                            + request.getTrainingDuration());
        }
        else {

            int result =
                    summary.getDuration()
                            - request.getTrainingDuration();

            if (result < 0) {
                throw new IllegalArgumentException(
                        "Monthly workload cannot be negative.");
            }

            summary.setDuration(result);
        }

        repository.save(trainer);
    }

    public MonthSummary getWorkload(String username, int year, Month month) {

        TrainerWorkload trainer =
                repository.findByUsername(username)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Trainer not found"));

        return trainer.getSummaries()
                        .stream()
                        .filter(s ->
                                s.getCalendarYear() == year &&
                                        s.getWorkloadMonth() == month)
                        .findFirst()
                        .orElse(new MonthSummary(year, month, 0, trainer));
    }

    private TrainerWorkload createTrainer(TrainerWorkloadRequest request) {

        TrainerWorkload trainer = new TrainerWorkload();

        trainer.setUsername(request.getUsername());
        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setActive(request.isActive());

        return trainer;
    }

    private MonthSummary findMonthSummary(TrainerWorkload trainer, int year, Month month) {

        return trainer.getSummaries()
                .stream()
                .filter(summary ->
                        summary.getCalendarYear().equals(year)
                                && summary.getWorkloadMonth() == month)
                .findFirst()
                .orElse(null);
    }
}
