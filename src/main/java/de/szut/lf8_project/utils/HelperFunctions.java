package de.szut.lf8_project.utils;

import de.szut.employees.EmployeeAPI;
import de.szut.employees.dto.EmployeeResponseDTO;
import de.szut.employees.dto.SkillDTO;
import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import de.szut.lf8_project.lf8.project.ProjectEntity;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class HelperFunctions {
    public static <T> boolean containsAny(Collection<T> source, Collection<T> contains) {
        for(T item : contains)
            if(source.contains(item))
                return true;
        return false;
    }

    public static void checkEmployee(Long id, ProjectEntity project, LocalDate startDate, LocalDate endDate, String authToken, TimeManagementService tmService) throws IOException {
        if(!tmService.hasEmployeeTime(id, startDate, endDate))
            throw new InvalidDataException(String.format("The employee with the ID: %d is already working on another project at that time!", id));

        EmployeeResponseDTO query = EmployeeAPI.getInstance().findEmployeeById(id, authToken);
        if(query == null)
            throw new InvalidDataException(String.format("The employee with the ID: %d was not found!", id));

        if(project.getSkillSet() == null)
            project.setSkillSet(new HashSet<>());

        if(project.getSkillSet().isEmpty())
            return;

        if(!HelperFunctions.containsAny(project.getSkillSet(), query.getSkillSet().stream().map(SkillDTO::getId).collect(Collectors.toSet())))
            throw new InvalidDataException(String.format("The employee with the ID: %d is not skilled enough for this project.", id));
    }
}
