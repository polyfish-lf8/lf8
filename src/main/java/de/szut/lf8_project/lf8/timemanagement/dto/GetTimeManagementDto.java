package de.szut.lf8_project.lf8.timemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class GetTimeManagementDto extends AddEmployeeToProjectDto{
    @NotNull
    private Long projectId;

    public GetTimeManagementDto(Long employeeId, Long projectId, LocalDate startDate, LocalDate endDate) {
        super(employeeId, startDate, endDate);
        this.projectId = projectId;
    }
}
