package de.szut.lf8_project.lf8.project;

import de.szut.employees.EmployeeAPI;
import de.szut.employees.dto.EmployeeResponseDTO;
import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import de.szut.lf8_project.lf8.project.dto.CreateProjectDto;
import de.szut.lf8_project.lf8.project.dto.GetProjectDto;
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
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

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
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @PostMapping(path = "create")
    public ResponseEntity<GetProjectDto> create(@RequestHeader("Authorization") String authToken, @RequestBody @Valid CreateProjectDto dto) throws IOException {
        ProjectEntity entity = mapper.mapCreateDtoToEntity(dto);

        List<EmployeeResponseDTO> employees = new ArrayList<>();
        if(entity.getEmployees() != null && !entity.getEmployees().isEmpty()) {
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

        return new ResponseEntity<>(mapper.mapToGetProjectDto(service.create(entity)), HttpStatus.CREATED);
    }

    @Operation(summary = "Get's a Project by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "getting successful", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @GetMapping (path = "get/{id}")
    public ResponseEntity<GetProjectDto> getById(@PathVariable final Long id) {
        final var entity = this.service.readById(id);
        final GetProjectDto dto = this.mapper.mapToGetProjectDto(entity);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Gets all of the created projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "list of projects", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })

    @GetMapping (path = "get")
    public List<GetProjectDto> findAll() {
        return service.readAll().stream().map(mapper::mapToGetProjectDto).collect(Collectors.toList());
    }

    @Operation(summary = "Deletes a project using it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "deletes the given project", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.NOT_FOUND, description = "project not found",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @DeleteMapping("delete/{projectId}")
    public void DeleteProject(@PathVariable Long projectId) {
        service.deleteProjectById(projectId);
    }

    @Operation(summary = "Gets the Employeelist of one Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "getting successful", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",

                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @GetMapping (path = "get/{id}/employees")
    public ResponseEntity<HashMap<Long, String>> getAllEmployeesByProjectId(@RequestHeader("Authorization") String authToken, @PathVariable final Long id) throws IOException {
        HashMap<Long, String> employeeResponseDTOList = new HashMap<>();
        final ProjectEntity entity = this.service.readById(id);
        for (long employeeID: entity.getEmployees()) {
            EmployeeResponseDTO foundEmployee = EmployeeAPI.getInstance().findEmployeeById(employeeID, authToken);
            employeeResponseDTOList.put(foundEmployee.getId(), String.format("%s %s", foundEmployee.getFirstName(), foundEmployee.getLastName()));
        }

        return new ResponseEntity<>(employeeResponseDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Updates the Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "update successful", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_FOUND, description = "not found"),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @PutMapping(path = "{id}")
    public ResponseEntity <GetProjectDto> update(@PathVariable final Long id, @RequestBody @Valid CreateProjectDto dto) {
        ProjectEntity projectEntity = service.readById(id);

        if (projectEntity == null) {
            throw new InvalidDataException("Project not found");
        }

        projectEntity.setEmployees(dto.getEmployees());
        projectEntity.setEndDate(dto.getEndDate());
        projectEntity.setStartDate(dto.getStartDate());
        projectEntity.setSkillSet(dto.getSkillSet());
        projectEntity.setDescription(dto.getDescription());
        projectEntity.setResponsibleEmployeeId(dto.getResponsibleEmployeeId());
        projectEntity.setResponsibleCustomerEmployeeId(dto.getResponsibleCustomerEmployeeId());

        return new ResponseEntity<>(mapper.mapToGetProjectDto(service.create(projectEntity)), HttpStatus.OK);
    }

    @Operation(summary = "Delete a projects employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "employee was deleted", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            //TODO: Fix the code here, it is not yet implemented it gets implemented in Waled's deletion task
            @ApiResponse(responseCode = "404", description = "not found",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @DeleteMapping (path = "{projectId}/delete/employee/{employeeId}")
    public void deleteEmployeeFromProject(@PathVariable Long employeeId, @PathVariable Long projectId) throws IOException {
        ProjectEntity project = service.readById(projectId);
        if(project == null)
            throw new InvalidDataException("Project not found");

        if(project.getEmployees().contains(employeeId))
            project.getEmployees().remove(employeeId);
        else
            throw new InvalidDataException("Employee could not be deleted");
    }

    @Operation(summary = "Gets all Projects of one Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "list of projects", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @GetMapping (path = "get/employee/{id}/projects")
    public List<GetProjectDto> getEmployeeProjects(@PathVariable final Long id){
        List<GetProjectDto> allProjects = findAll();
        List<GetProjectDto> returnValue = new ArrayList<>();

        for (GetProjectDto project: allProjects) {
            if(project.getEmployees() == null)
                throw new InvalidDataException("Project Employee List is empty");

            if(project.getEmployees().contains(id))
                returnValue.add(project);

        }
        return returnValue;
    }
}