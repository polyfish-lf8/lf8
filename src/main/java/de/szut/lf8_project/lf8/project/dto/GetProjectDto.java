package de.szut.lf8_project.lf8.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
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
    private Set<Long> employees;
    private Set<String> skillSet;
    private LocalDate startDate;
    private LocalDate endDate;
}
