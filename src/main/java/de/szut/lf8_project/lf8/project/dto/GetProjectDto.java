package de.szut.lf8_project.lf8.project.dto;

import de.szut.lf8_project.lf8.timemanagement.dto.GetTimeManagementDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class GetProjectDto {
    private Long id;
    private Long customerId;
    private Long responsibleEmployeeId;
    private Long responsibleCustomerEmployeeId;
    private String description;
    private Set<GetTimeManagementDto> employees;
    private Set<Long> skillSet;
    private LocalDate startDate;
    private LocalDate endDate;
}
