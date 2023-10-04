package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.hello.HelloEntity;
import de.szut.lf8_project.hello.dto.HelloCreateDto;
import de.szut.lf8_project.hello.dto.HelloGetDto;
import de.szut.lf8_project.lf8.project.dto.ProjectCreateDto;
import de.szut.lf8_project.lf8.project.dto.ProjectGetDto;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {
    public ProjectGetDto mapToGetDto(ProjectEntity entity) {
        return new ProjectGetDto(entity.getId(), entity.getCustomerId(), entity.getResponsibleEmployeeId(), entity.getResponsibleCustomerEmployeeId(), entity.getDescription());
    }

    public ProjectEntity mapCreateDtoToEntity(ProjectCreateDto dto) {
        ProjectEntity entity = new ProjectEntity();

        entity.setCustomerId(dto.getCustomerId());
        entity.setResponsibleEmployeeId(dto.getResponsibleEmployeeId());
        entity.setResponsibleCustomerEmployeeId(dto.getResponsibleCustomerEmployeeId());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
