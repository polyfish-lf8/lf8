package de.szut.lf8_project.lf8.timemanagement;

import de.szut.lf8_project.lf8.project.ProjectEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimeManagementService {
    private final TimeManagementRepository repository;

    public TimeManagementEntity create(TimeManagementEntity entity) {
        return repository.save(entity);
    }

    public boolean hasEmployeeTime(Long employeeID, LocalDate startDate, LocalDate endDate) {
        return !repository.existsByEmployeeIdAndWorkingDates(employeeID, startDate, endDate);
    }

    @Transactional
    public void deleteAllByProjectId(Long projectId) {
        repository.deleteAllByProjectId(projectId);
    }

    public List<ProjectEntity> findProjectsByEmployeeId(Long id) {
        return repository.findProjectsByEmployeeId(id);
    }

    @Transactional
    public void deleteByEmployeeIdAndProjectId(Long employeeId, Long projectId) {
        repository.deleteByEmployeeIdAndProjectId(employeeId, projectId);
    }

    public List<TimeManagementEntity> readAllById(Collection<Long> ids) {
        return repository.findAll().stream().filter(f -> ids.contains(f.getId())).collect(Collectors.toList());
    }

    public TimeManagementEntity readByEmployeeIdAndProjectId(Long employeeId, Long projectId) {
        return repository.findByEmployeeIdAndProjectId(employeeId, projectId);
    }
}