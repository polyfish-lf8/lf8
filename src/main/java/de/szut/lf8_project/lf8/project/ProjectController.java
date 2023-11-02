package de.szut.lf8_project.lf8.project;

import de.szut.employees.EmployeeAPI;
import de.szut.employees.dto.EmployeeResponseDTO;
import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import de.szut.lf8_project.lf8.timemanagement.dto.AddEmployeeToProjectDto;
import de.szut.lf8_project.lf8.project.dto.CreateProjectDto;
import de.szut.lf8_project.lf8.project.dto.GetProjectDto;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementEntity;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementService;
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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(value = "lf8/project")
public class ProjectController {
    private final TimeManagementService tmService;
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
                TimeManagementEntity tmEntity = new TimeManagementEntity();

                tmEntity.setProjectId(entity.getId());
                tmEntity.setStartWorkingDate(employee.getStartDate());
                tmEntity.setEndWorkingDate(employee.getEndDate());
                tmEntity.setEmployeeId(employee.getEmployeeId());

                tmService.create(tmEntity);
                
                entity.getEmployees().add(tmEntity);
            }

            service.create(entity);
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
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @GetMapping (path = "{projectId}")
    public ResponseEntity<GetProjectDto> getById(@PathVariable final Long projectId) {
        final var entity = this.service.readById(projectId);
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
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })
    @GetMapping (path = "{projectId}/employees")
    public ResponseEntity<HashMap<Long, String>> getAllEmployeesByProjectId(@RequestHeader("Authorization") String authToken, @PathVariable final Long projectId) throws IOException {
        HashMap<Long, String> employeeResponseDTOList = new HashMap<>();
        final ProjectEntity entity = this.service.readById(projectId);
        for (TimeManagementEntity employeeID : entity.getEmployees()) {
            EmployeeResponseDTO foundEmployee = EmployeeAPI.getInstance().findEmployeeById(employeeID.getEmployeeId(), authToken);
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
            @ApiResponse(responseCode = HTTPCodes.INTERNAL_SERVER_ERROR, description = "internal server error",
                    content = @Content)
    })

    @PostMapping(path = "{projectId}/employees")
    public ResponseEntity<GetProjectDto> addEmployeeToProject(@RequestHeader("Authorization") String authToken, @PathVariable Long projectId, @RequestBody @Valid AddEmployeeToProjectDto dto) throws IOException {
        ProjectEntity entity = service.readById(projectId);
        if(entity == null)
            throw new InvalidDataException("The project was not found!");

        HelperFunctions.checkEmployee(dto.getEmployeeId(), entity, dto.getStartDate(), dto.getEndDate(), authToken, tmService);

        TimeManagementEntity tmEntity = new TimeManagementEntity();
        tmEntity.setProjectId(entity.getId());
        tmEntity.setEmployeeId(dto.getEmployeeId());
        tmEntity.setStartWorkingDate(dto.getStartDate());
        tmEntity.setEndWorkingDate(dto.getEndDate());

        tmService.create(tmEntity);

        entity.getEmployees().add(tmEntity);

        return new ResponseEntity<>(mapper.mapToGetProjectDto(service.create(entity)), HttpStatus.CREATED);
    }
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