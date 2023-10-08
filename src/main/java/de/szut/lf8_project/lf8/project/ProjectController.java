package de.szut.lf8_project.lf8.project;

import de.szut.employees.EmployeeAPI;
import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import de.szut.lf8_project.lf8.project.dto.ProjectCreateDto;
import de.szut.lf8_project.lf8.project.dto.ProjectGetDto;
import de.szut.lf8_project.utils.HTTPCodes;
import de.szut.lf8_project.utils.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
    public ResponseEntity<ProjectGetDto> create(@RequestHeader("Authorization") String authToken, @RequestBody @Valid ProjectCreateDto dto) throws IOException {
        ProjectEntity entity = mapper.mapCreateDtoToEntity(dto);
        if(!entity.getEmployees().isEmpty()) {
            for(Long employeeID : entity.getEmployees()) {
                int qualifications = 0;
                ArrayList<String> employeeSkillSet = EmployeeAPI.getInstance().findEmployeeById(employeeID, authToken).getSkillSet();
                if(employeeSkillSet.size() > 0) {
                    for (String skill : employeeSkillSet) {
                        if(entity.getSkillSet().contains(skill)) {
                            qualifications++;
                        }
                    }
                }
                if(qualifications < 1) {
                    throw new InvalidDataException("Not skilled enough for this project.");
                }
            }
        }

        return new ResponseEntity<>(mapper.mapToGetDto(service.create(entity)), HttpStatus.CREATED);
    }

    @Operation(summary = "Gets all of the created projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "list of projects", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = ProjectGetDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @GetMapping (path = "findall")
    public List<ProjectGetDto> findAll(){
        return  service.readAll().stream().map(e -> mapper.mapToGetDto(e)).collect(Collectors.toList());
    }
}
