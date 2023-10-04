package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.hello.dto.HelloGetDto;
import de.szut.lf8_project.lf8.project.dto.ProjectCreateDto;
import de.szut.lf8_project.lf8.project.dto.ProjectGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Creates a new ProjectEntity using its required parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  "201", description = "creation successful", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectGetDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "not authorized",
                    content = @Content)
    })
    public ProjectGetDto create(@RequestBody @Valid ProjectCreateDto dto) {
        return null;
    }
}
