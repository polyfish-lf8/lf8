package de.szut.lf8_project.lf8.project.dto;

import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class CreateProjectDto {

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
    private Set<Long> employees;

    @Size(min = 1)
    private Set<String> skillSet;

    @Size(min = 1)
    @NotNull
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    public CreateProjectDto(Long customerId, Long responsibleEmployeeId, Long responsibleCustomerEmployeeId, Set<Long> employees, Set<String> skillSet, String description, LocalDate startDate, LocalDate endDate) {
        this.customerId = customerId;
        this.responsibleEmployeeId = responsibleEmployeeId;
        this.responsibleCustomerEmployeeId = responsibleCustomerEmployeeId;
        this.employees = employees;
        this.skillSet = skillSet;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;

        if((startDate != null && endDate != null) && startDate.isAfter(endDate))
            throw new InvalidDataException("The dates are not correct");
    }
}
