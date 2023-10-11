package de.szut.lf8_project.lf8.project.dto;

import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    private List<Long> employees;

    @Size(min = 1)
    private List<String> skillSet;

    @Size(min = 1)
    @NotNull
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    public CreateProjectDto(Long customerId, Long responsibleEmployeeId, Long responsibleCustomerEmployeeId, List<Long> employees, List<String> skillSet, String description, LocalDate startDate, LocalDate endDate) {
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

        if(employees != null && !employees.isEmpty())
            for(Long i : employees) {
                if(employees.stream().filter(employee -> Objects.equals(employee, i)).count() > 1)
                    throw new InvalidDataException(MessageFormat.format("The employee with the ID: {0} is given more than one time", i));
            }
    }
}
