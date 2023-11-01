package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.lf8.timemanagement.TimeManagementEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

public class getAllEmployeesOfOneProjectIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    public void getAllEmployeesByProjectId() throws Exception {
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, new HashSet<>(), new HashSet<>(), "Hallo Jana", LocalDate.now(), LocalDate.now().plusDays(2)));
        HashSet<TimeManagementEntity> employees = new HashSet<>(
                Set.of(
                        new TimeManagementEntity(0L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(1)),
                        new TimeManagementEntity(0L, 1L, 2L, LocalDate.now(), LocalDate.now().plusDays(1))
                )
        );

        mockProject.setEmployees(new HashSet<>(timeManagementRepository.saveAll(employees)));
        projectRepository.save(mockProject);

        this.mockMvc.perform(get(String.format("/lf8/project/%d/employees", mockProject.getId())).header("Authorization", token))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.1", is("Max Mustermann")))
                .andExpect(jsonPath("$.2", is("Susanne Musterfrau")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
