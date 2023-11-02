package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashSet;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddEmployeeIT extends AbstractIntegrationTest {
    @Test
    @WithMockUser(roles = "user")
    public void addEmployeeToProject() throws Exception {
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, new HashSet<>(), new HashSet<>(), "Hallo Jana", LocalDate.now(), LocalDate.now().plusDays(2)));

        String content = """
                {
                    "employeeId": 1,
                    "startDate": "2023-12-01",
                    "endDate": "2023-12-02"
                }
                """;
        this.mockMvc.perform(
                        post(String.format("/lf8/project/%d/employees", mockProject.getId()))
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("employees[0].employeeId", is(1)))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @WithMockUser(roles = "user")
    public void tryToAddNotExistingUser() throws Exception {
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, new HashSet<>(), new HashSet<>(), "Hallo Jana", LocalDate.now(), LocalDate.now().plusDays(2)));

        String content = """
                {
                    "employeeId": 999999999,
                    "startDate": "2023-11-01",
                    "endDate": "2023-11-02"
                }
            """;

        this.mockMvc.perform(
                        post(String.format("/lf8/project/%d/employees", mockProject.getId()))
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @ParameterizedTest()
    @WithMockUser(roles = "user")
    @ValueSource(strings = {
            """
                {
                    "employeeId": 1,
                    "startDate": "2023-11-03",
                    "endDate": "2023-11-02"
                }
            """,
            """
                {
                    "employeeId": 1,
                    "startDate": "2023-11-01",
                    "endDate": "2023-11-00"
                }
            """,
            """
                {
                    "employeeId": 1,
                    "startDate": "2023-11-01",
                    "endDate": "2023-10-02"
                }
            """,
            """
                {
                    "employeeId": "abc",
                    "startDate": "2023-11-01",
                    "endDate": "2023-11-02"
                }
            """,
    })
    public void addEmployeeWithWrongData(String content) throws Exception {
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, new HashSet<>(), new HashSet<>(), "Hallo Jana", LocalDate.now(), LocalDate.now().plusDays(2)));

        this.mockMvc.perform(
                        post(String.format("/lf8/project/%d/employees", mockProject.getId()))
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }
}
