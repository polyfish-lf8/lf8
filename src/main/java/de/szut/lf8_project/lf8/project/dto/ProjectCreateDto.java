package de.szut.lf8_project.lf8.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectCreateDto {
    private Long customerId;
    private Long responsibleEmployeeId;
    private Long responsibleCustomerEmployeeId;
    private String description;
}
