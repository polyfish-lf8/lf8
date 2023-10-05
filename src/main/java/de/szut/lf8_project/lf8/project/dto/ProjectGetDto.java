package de.szut.lf8_project.lf8.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectGetDto {
    private Long id;
    private Long customerId;
    private Long responsibleEmployeeId;
    private Long responsibleCustomerEmployeeId;
    private String description;
    private List<Long> employees;
    private List<String> skills;
    private LocalDate startDate;
    private LocalDate endDate;
}
