package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.lf8.project.dto.ProjectCreateDto;
import de.szut.lf8_project.lf8.project.dto.ProjectGetDto;
import de.szut.lf8_project.utils.HTTPCodes;
import de.szut.lf8_project.utils.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "lf8/project")
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper mapper;

    public ProjectController(ProjectService service, ProjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Creates a new project using its required parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.CREATED, description = "creation successful", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = ProjectGetDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @PostMapping(path = "create")
    public ProjectGetDto create(@RequestBody @Valid ProjectCreateDto dto) {
        return mapper.mapToGetDto(service.create(mapper.mapCreateDtoToEntity(dto)));
    }
}
