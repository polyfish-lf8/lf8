package de.szut.lf8_project.lf8.project;

import de.szut.employees.EmployeeAPI;
import de.szut.employees.dto.EmployeeResponseDTO;
import de.szut.lf8_project.exceptionHandling.InvalidDataException;
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
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "lf8/project")
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper mapper;
    private final Map<Integer, ProjectController> projects = new HashMap<>();
    private final Map<Integer, String> projectLeaders = new HashMap<>();

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

        List<EmployeeResponseDTO> employees = new ArrayList<>();
        if(entity.getEmployees() != null) {
            for(Long id : entity.getEmployees()) {
                EmployeeResponseDTO queryResult = EmployeeAPI.getInstance().findEmployeeById(id, authToken);

                if(queryResult == null)
                    throw new InvalidDataException(MessageFormat.format("The employee with the ID: {0} was not found!", id));

                employees.add(queryResult);
            }
        }

        if(entity.getEmployees() != null && !entity.getEmployees().isEmpty()) {
            for(EmployeeResponseDTO employee : employees) {
                int qualifications = 0;
                if(employee.getSkillSet() != null && !employee.getSkillSet().isEmpty()) {
                    for (String skill : employee.getSkillSet()) {
                        if(entity.getSkillSet() != null && entity.getSkillSet().contains(skill)) {
                            qualifications++;
                        }
                    }
                }
                if(qualifications > 0) {
                    throw new InvalidDataException("Not skilled enough for this project.");
                }
            }
        }

        return new ResponseEntity<>(mapper.mapToGetDto(service.create(entity)), HttpStatus.CREATED);
    }
}
