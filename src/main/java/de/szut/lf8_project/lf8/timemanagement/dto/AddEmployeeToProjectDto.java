package de.szut.lf8_project.lf8.timemanagement.dto;

import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class AddEmployeeToProjectDto {
    @NotNull
    private Long employeeId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public AddEmployeeToProjectDto(Long employeeId, LocalDate startDate, LocalDate endDate) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;

        if(startDate.isAfter(endDate))
            throw new InvalidDataException("The dates are not correct");
    }
}
