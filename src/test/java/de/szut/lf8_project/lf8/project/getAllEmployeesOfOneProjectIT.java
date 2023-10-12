package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

public class getAllEmployeesOfOneProjectIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    public void getAllEmployeesByProjectId() throws Exception {
        HashSet<Long> e = new HashSet<>(Set.of(1L, 2L));
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, e, new HashSet<>(), "Hallo Jana", LocalDate.now(), LocalDate.now().plusDays(2)));

        this.mockMvc.perform(get(String.format("/lf8/project/get/%d/employees", mockProject.getId())).header("Authorization", token))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("1", is("Max Mustermann")))
                .andExpect(jsonPath("2", is("Susanne Musterfrau")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
