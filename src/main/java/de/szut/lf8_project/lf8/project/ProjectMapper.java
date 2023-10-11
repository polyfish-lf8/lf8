package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.lf8.project.dto.CreateProjectDto;
import de.szut.lf8_project.lf8.project.dto.GetProjectDto;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {
    public GetProjectDto mapToGetDto(ProjectEntity entity) {
        return new GetProjectDto(
                entity.getId(),
                entity.getCustomerId(),
                entity.getResponsibleEmployeeId(),
                entity.getResponsibleCustomerEmployeeId(),
                entity.getDescription(),
                entity.getEmployees(),
                entity.getSkillSet(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }

    public ProjectEntity mapCreateDtoToEntity(CreateProjectDto dto) {
        ProjectEntity entity = new ProjectEntity();

        entity.setCustomerId(dto.getCustomerId());
        entity.setResponsibleEmployeeId(dto.getResponsibleEmployeeId());
        entity.setResponsibleCustomerEmployeeId(dto.getResponsibleCustomerEmployeeId());
        entity.setDescription(dto.getDescription());
        entity.setEmployees(dto.getEmployees());
        entity.setSkillSet(dto.getSkillSet());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        return entity;
    }
}
