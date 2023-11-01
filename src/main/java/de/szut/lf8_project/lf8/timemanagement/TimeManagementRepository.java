package de.szut.lf8_project.lf8.timemanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TimeManagementRepository extends JpaRepository<TimeManagementEntity, Long> {
    @Query("SELECT COUNT(tm) > 0 FROM TimeManagementEntity tm " +
            "WHERE tm.employeeId = :employeeId " +
            "AND tm.startWorkingDate <= :endDate " +
            "AND tm.endWorkingDate >= :startDate")
    boolean existsByEmployeeIdAndWorkingDates(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
