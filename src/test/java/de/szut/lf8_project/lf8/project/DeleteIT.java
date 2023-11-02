package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteIT extends AbstractIntegrationTest {
    @Test
    public void authorization() throws Exception {
        this.mockMvc.perform(post("/lf8/project/get"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    public void DeleteProject() throws Exception {
        ProjectEntity entity = projectRepository.save(new ProjectEntity());
                this.mockMvc.perform(
                 delete(String.format("/lf8/project/delete/%d", entity.getId())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "user")
    public void DeleteEmployeeFromProject() throws Exception {
        Set<Long> employees =  new HashSet<Long>();
        employees.add(1L);
        employees.add(2L);
        ProjectEntity ent1 = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, employees, new HashSet<>(), "Hallo Arad", LocalDate.now(), LocalDate.now().plusDays(2)));

        this.mockMvc
                .perform(delete(String.format("/lf8/project/%d/delete/employee/1", ent1.getId())))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}