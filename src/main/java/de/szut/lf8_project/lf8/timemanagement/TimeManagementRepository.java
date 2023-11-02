package de.szut.lf8_project.lf8.timemanagement;

import de.szut.lf8_project.lf8.project.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TimeManagementRepository extends JpaRepository<TimeManagementEntity, Long> {
    @Query("SELECT COUNT(tm) > 0 FROM TimeManagementEntity tm " +
            "WHERE tm.employeeId = :employeeId " +
            "AND tm.startWorkingDate <= :endDate " +
            "AND tm.endWorkingDate >= :startDate")
    boolean existsByEmployeeIdAndWorkingDates(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    void deleteAllByProjectId(Long projectId);
    @Query("SELECT p FROM ProjectEntity p JOIN TimeManagementEntity t ON p.id = t.projectId WHERE t.employeeId = :employeeId")
    List<ProjectEntity> findProjectsByEmployeeId(Long employeeId);
}
