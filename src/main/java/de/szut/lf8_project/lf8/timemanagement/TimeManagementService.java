package de.szut.lf8_project.lf8.timemanagement;

import de.szut.lf8_project.lf8.project.ProjectEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public void deleteAllByProjectId(Long projectId) {
        repository.deleteAllByProjectId(projectId);
    }

    public List<ProjectEntity> findProjectsByEmployeeId(Long id) {
        return repository.findProjectsByEmployeeId(id);
    }
}