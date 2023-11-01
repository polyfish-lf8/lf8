package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeGetIT extends AbstractIntegrationTest {

    @Test
    public void authorization() throws Exception {
        this.mockMvc.perform(post("/lf8/project/get"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void getEmployeeProjects() throws Exception {
        Set<Long> employees =  new HashSet<Long>();
        employees.add(Integer.toUnsignedLong(1));
        employees.add(Integer.toUnsignedLong(2));
        var ent1 = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, employees, new HashSet<>(), "Hallo Jana", LocalDate.now(), LocalDate.now().plusDays(2)));

        final var contentAsString = this.mockMvc.perform(get("/lf8/project/get/employee/1/projects"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ent1.getId().intValue())));
    }
}
