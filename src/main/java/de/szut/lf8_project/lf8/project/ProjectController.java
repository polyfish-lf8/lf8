package de.szut.lf8_project.lf8.project;

import de.szut.employees.EmployeeAPI;
import de.szut.employees.dto.EmployeeResponseDTO;
import de.szut.lf8_project.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_project.lf8.project.dto.CreateProjectDto;
import de.szut.lf8_project.lf8.project.dto.GetProjectDto;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementEntity;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementService;
import de.szut.lf8_project.lf8.timemanagement.dto.AddEmployeeToProjectDto;
import de.szut.lf8_project.utils.HTTPCodes;
import de.szut.lf8_project.utils.HelperFunctions;
import de.szut.lf8_project.utils.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(value = "lf8/project")
public class ProjectController {
    private final TimeManagementService timeManagementService;
    private final ProjectService service;
    private final ProjectMapper mapper;

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
    @PostMapping
    public ResponseEntity<GetProjectDto> create(@RequestHeader("Authorization") String authToken, @RequestBody @Valid CreateProjectDto dto) throws IOException {
        ProjectEntity entity = service.create(mapper.mapCreateDtoToEntity(dto, authToken));

        if(dto.getEmployees() != null && !dto.getEmployees().isEmpty()) {
            for (AddEmployeeToProjectDto employee : dto.getEmployees()) {
                TimeManagementEntity timeManagementEntity = new TimeManagementEntity();

                timeManagementEntity.setProjectId(entity.getId());
                timeManagementEntity.setStartWorkingDate(employee.getStartDate());
                timeManagementEntity.setEndWorkingDate(employee.getEndDate());
                timeManagementEntity.setEmployeeId(employee.getEmployeeId());

                timeManagementService.create(timeManagementEntity);

                entity.getEmployees().add(timeManagementEntity.getId());
            }

            entity = service.create(entity);
        }

        return new ResponseEntity<>(mapper.mapToGetProjectDto(entity), HttpStatus.CREATED);
    }

    @Operation(summary = "Gets a project by the project ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "getting successful", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_FOUND, description = "the project was not found",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @GetMapping (path = "{projectId}")
    public ResponseEntity<GetProjectDto> getById(@PathVariable final Long projectId) {
        final var entity = this.service.readById(projectId);

        if(entity == null)
            throw new ResourceNotFoundException("The project was not found");

        final GetProjectDto dto = this.mapper.mapToGetProjectDto(entity);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Gets every project from the database")
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

    @GetMapping
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
    @DeleteMapping("{projectId}")
    public void DeleteProject(@PathVariable Long projectId) {
        if(service.readById(projectId) == null)
            throw new ResourceNotFoundException("The project was not found");

        service.deleteProjectById(projectId);
    }

    @Operation(summary = "Gets the list of employee working on the project")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "getting successful", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_FOUND, description = "the project was not found",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @GetMapping (path = "{projectId}/employees")
    public ResponseEntity<HashMap<Long, String>> getAllEmployeesByProjectId(@RequestHeader("Authorization") String authToken, @PathVariable final Long projectId) throws IOException {
        HashMap<Long, String> employeeResponseDTOList = new HashMap<>();
        final ProjectEntity entity = this.service.readById(projectId);

        if(entity == null)
            throw new ResourceNotFoundException("The project was not found");

        for (TimeManagementEntity employee : timeManagementService.readAllById(entity.getEmployees())) {
            EmployeeResponseDTO foundEmployee = EmployeeAPI.getInstance().findEmployeeById(employee.getEmployeeId(), authToken);
            employeeResponseDTOList.put(foundEmployee.getId(), String.format("%s %s", foundEmployee.getFirstName(), foundEmployee.getLastName()));
        }

        return new ResponseEntity<>(employeeResponseDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Adds an employee to a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode =  HTTPCodes.CREATED, description = "The employee was successful added to the project", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "invalid JSON posted",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_AUTHORIZED, description = "not authorized",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_FOUND, description = "The project was not found",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })

    @PostMapping(path = "{projectId}/employees")
    public ResponseEntity<GetProjectDto> addEmployeeToProject(@RequestHeader("Authorization") String authToken, @PathVariable Long projectId, @RequestBody @Valid AddEmployeeToProjectDto dto) throws IOException {
        ProjectEntity entity = service.readById(projectId);
        if(entity == null)
            throw new ResourceNotFoundException("The project was not found!");

        HelperFunctions.checkEmployee(dto.getEmployeeId(), entity, dto.getStartDate(), dto.getEndDate(), authToken, timeManagementService);

        TimeManagementEntity tmEntity = new TimeManagementEntity();
        tmEntity.setProjectId(entity.getId());
        tmEntity.setEmployeeId(dto.getEmployeeId());
        tmEntity.setStartWorkingDate(dto.getStartDate());
        tmEntity.setEndWorkingDate(dto.getEndDate());

        timeManagementService.create(tmEntity);

        entity.getEmployees().add(tmEntity.getId());

        return new ResponseEntity<>(mapper.mapToGetProjectDto(service.create(entity)), HttpStatus.CREATED);
    }

    @Operation(summary = "Updates the Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTPCodes.SUCCESSFUL, description = "update successful", content = @Content),
            @ApiResponse(responseCode = HTTPCodes.NOT_FOUND, description = "the project was not found"),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @PutMapping(path = "{id}")
    public ResponseEntity <GetProjectDto> update(@RequestHeader("Authorization") String authToken, @PathVariable final Long id, @RequestBody @Valid CreateProjectDto dto) throws IOException {
        ProjectEntity projectEntity = service.readById(id);
        mapper.mapCreateDtoToEntity(dto, authToken);

        if (projectEntity == null)
            throw new ResourceNotFoundException("Project not found");

        if(dto.getEmployees() != null) {
            Set<Long> processedEmployees = new HashSet<>();
            timeManagementService.deleteAllByProjectId(id);

            if(!dto.getEmployees().isEmpty()) {
                dto.getEmployees().forEach(i -> processedEmployees.add(
                        timeManagementService.create(
                                new TimeManagementEntity(0L, projectEntity.getId(), i.getEmployeeId(), i.getStartDate(), i.getEndDate())
                        ).getId()
                ));
            }

            projectEntity.setEmployees(processedEmployees);
        }

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
            @ApiResponse(responseCode =  HTTPCodes.SUCCESSFUL, description = "Employee was deleted", content = {
                    @Content(mediaType = MediaTypes.JSON,
                            schema = @Schema(implementation = GetProjectDto.class))
            }),
            @ApiResponse(responseCode = HTTPCodes.NOT_FOUND, description = "Project not found",
                    content = @Content),
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @DeleteMapping (path = "{projectId}/employees/{employeeId}")
    public ResponseEntity<GetProjectDto> deleteEmployeeFromProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
        ProjectEntity project = service.readById(projectId);
        if(project == null)
            throw new ResourceNotFoundException("Project not found");

        if (project.getEmployees() == null)
            return new ResponseEntity<>(mapper.mapToGetProjectDto(project), HttpStatus.OK);

        TimeManagementEntity tmEntity = timeManagementService.readByEmployeeIdAndProjectId(employeeId, projectId);

        if(tmEntity != null)
            project.getEmployees().remove(tmEntity.getId());

        timeManagementService.deleteByEmployeeIdAndProjectId(employeeId, projectId);

        return new ResponseEntity<>(mapper.mapToGetProjectDto(project), HttpStatus.OK);
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
    @GetMapping (path = "employees/{id}/projects")
    public ResponseEntity<List<GetProjectDto>> getEmployeeProjects(@PathVariable final Long id) {
        ArrayList<GetProjectDto> result = new ArrayList<>();
        timeManagementService.findProjectsByEmployeeId(id).forEach(project -> result.add(mapper.mapToGetProjectDto(project)));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
