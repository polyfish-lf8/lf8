package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.lf8.timemanagement.TimeManagementEntity;
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
                 delete(String.format("/lf8/project/%d", entity.getId())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "user")
    public void DeleteEmployeeFromProject() throws Exception {
        Set<TimeManagementEntity> e = new HashSet<>();
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, e, new HashSet<>(), "Hallo Waled", LocalDate.now(), LocalDate.now().plusDays(2)));

        e.add(timeManagementRepository.save(new TimeManagementEntity(0L, mockProject.getId(), 1L, LocalDate.now(), LocalDate.now().plusDays(2))));
        e.add(timeManagementRepository.save(new TimeManagementEntity(0L, mockProject.getId(), 1L, LocalDate.now(), LocalDate.now().plusDays(2))));

        mockProject.setEmployees(e);
        projectRepository.save(mockProject);

        this.mockMvc
                .perform(delete(String.format("/lf8/project/%d/delete/employee/1", mockProject.getId())))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}