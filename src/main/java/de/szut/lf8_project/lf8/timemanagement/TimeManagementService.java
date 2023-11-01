package de.szut.lf8_project.lf8.timemanagement;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TimeManagementService {
    private final TimeManagementRepository repository;

    public TimeManagementService(TimeManagementRepository repository) {
        this.repository = repository;
    }

    public TimeManagementEntity create(TimeManagementEntity entity) {
        return repository.save(entity);
    }

    public boolean hasEmployeeTime(Long employeeID, LocalDate startDate, LocalDate endDate) {
        return !repository.existsByEmployeeIdAndWorkingDates(employeeID, startDate, endDate);
    }
}