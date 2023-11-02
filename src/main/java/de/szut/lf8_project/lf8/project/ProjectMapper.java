package de.szut.lf8_project.lf8.project;

import de.szut.employees.EmployeeAPI;
import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import de.szut.lf8_project.lf8.project.dto.CreateProjectDto;
import de.szut.lf8_project.lf8.project.dto.GetProjectDto;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementEntity;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementService;
import de.szut.lf8_project.lf8.timemanagement.dto.GetTimeManagementDto;
import de.szut.lf8_project.utils.HelperFunctions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectMapper {
    private TimeManagementService tmService;
    private ProjectService service;

    public GetProjectDto mapToGetProjectDto(ProjectEntity entity) {
        Set<GetTimeManagementDto> timeManagedEmployees = new HashSet<>();

        for (TimeManagementEntity employee : tmService.readAllById(entity.getEmployees())) {
            timeManagedEmployees.add(mapToGetTimeManagementDto(employee));
        }

        return new GetProjectDto(
                entity.getId(),
                entity.getCustomerId(),
                entity.getResponsibleEmployeeId(),
                entity.getResponsibleCustomerEmployeeId(),
                entity.getDescription(),
                timeManagedEmployees,
                entity.getSkillSet(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }

    public ProjectEntity mapCreateDtoToEntity(CreateProjectDto dto, String authToken) throws IOException {
        ProjectEntity entity = new ProjectEntity();

        if(EmployeeAPI.getInstance().findEmployeeById(dto.getResponsibleEmployeeId(), authToken) == null)
            throw new InvalidDataException(String.format("No employee with the ID %d found", dto.getResponsibleEmployeeId()));

        entity.setCustomerId(dto.getCustomerId());
        entity.setResponsibleEmployeeId(dto.getResponsibleEmployeeId());
        entity.setResponsibleCustomerEmployeeId(dto.getResponsibleCustomerEmployeeId());
        entity.setDescription(dto.getDescription());
        entity.setSkillSet(dto.getSkillSet());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        if(dto.getEmployees() == null || dto.getEmployees().isEmpty())
            return entity;

        dto.getEmployees().forEach(timeManagedEmployeeDto -> {
            try {
                HelperFunctions.checkEmployee(timeManagedEmployeeDto.getEmployeeId(), entity, dto.getStartDate(), dto.getEndDate(), authToken, tmService);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return entity;
    }

    public GetTimeManagementDto mapToGetTimeManagementDto(TimeManagementEntity entity) {
        return new GetTimeManagementDto(entity.getEmployeeId(), entity.getProjectId(), entity.getStartWorkingDate(), entity.getEndWorkingDate());
    }
}
