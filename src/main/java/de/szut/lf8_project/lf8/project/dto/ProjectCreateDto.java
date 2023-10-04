package de.szut.lf8_project.lf8.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectCreateDto {

    @Min(0)
    @NotNull
    private Long customerId;

    @Min(0)
    @NotNull
    private Long responsibleEmployeeId;

    @Min(0)
    @NotNull
    private Long responsibleCustomerEmployeeId;

    @Size(min = 1)
    private List<Long> employees;

    @Size(min = 1)
    @NotNull
    private String description;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}