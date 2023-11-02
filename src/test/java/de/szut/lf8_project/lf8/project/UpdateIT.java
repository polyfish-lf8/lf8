package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.lf8.timemanagement.TimeManagementEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateIT extends AbstractIntegrationTest {
    @Test
    @WithMockUser(roles = "user")
    public void updateProject() throws Exception {
        Set<Long> e = new HashSet<>();
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(0L, 3L, 4L, 1L, e, new HashSet<>(), "Hallo Waled", LocalDate.now(), LocalDate.now().plusDays(2)));

        e.add(timeManagementRepository.save(new TimeManagementEntity(0L, mockProject.getId(), 1L, LocalDate.now(), LocalDate.now().plusDays(2))).getId());
        e.add(timeManagementRepository.save(new TimeManagementEntity(0L, mockProject.getId(), 2L, LocalDate.now(), LocalDate.now().plusDays(2))).getId());

        mockProject.setEmployees(e);
        mockProject = projectRepository.save(mockProject);

        String content = String.format("""
                {
                    "customerId": %d,
                    "responsibleEmployeeId": %d,
                    "responsibleCustomerEmployeeId": %d,
                    "description": ":)"
                }
                """, mockProject.getCustomerId(), mockProject.getResponsibleEmployeeId(), mockProject.getResponsibleCustomerEmployeeId());

        this.mockMvc.perform(
                        put(String.format("/lf8/project/%d", mockProject.getId()))
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id", is(mockProject.getId().intValue())))
                .andExpect(jsonPath("description", is(":)")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @WithMockUser(roles = "user")
    public void updateProjectWrongData() throws Exception {
        Set<Long> e = new HashSet<>();
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, e, new HashSet<>(), "Hallo Waled", LocalDate.now(), LocalDate.now().plusDays(2)));

        e.add(timeManagementRepository.save(new TimeManagementEntity(0L, mockProject.getId(), 1L, LocalDate.now(), LocalDate.now().plusDays(2))).getId());
        e.add(timeManagementRepository.save(new TimeManagementEntity(0L, mockProject.getId(), 1L, LocalDate.now(), LocalDate.now().plusDays(2))).getId());

        mockProject.setEmployees(e);
        projectRepository.save(mockProject);

        String content = String.format("""
                {
                    "customerId": %d,
                    "responsibleEmployeeId": 987987987,
                    "responsibleCustomerEmployeeId": %d,
                    "description": ":)"
                }
                """, mockProject.getCustomerId(), mockProject.getResponsibleCustomerEmployeeId());

        this.mockMvc.perform(
                        put(String.format("/lf8/project/%d", mockProject.getId()))
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
