package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.lf8.project.dto.ProjectCreateDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "project")

public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper mapper;

    public ProjectController(ProjectService service, ProjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    public ProjectCreateDto create(@RequestBody @Valid ProjectCreateDto dto) {
        return null;
    }
}
