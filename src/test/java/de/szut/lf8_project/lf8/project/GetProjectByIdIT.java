package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


public class GetProjectByIdIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    public void getById() throws Exception {
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, new HashSet<>(), new HashSet<>(), "Hallo Jana", LocalDate.now(), LocalDate.now().plusDays(2)));

        this.mockMvc.perform(
                get(String.format("/lf8/project/get/%d", mockProject.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id", is(mockProject.getId().intValue())))
                .andExpect(jsonPath("customerId", is(mockProject.getCustomerId().intValue())))
                .andExpect(jsonPath("responsibleCustomerEmployeeId", is(mockProject.getResponsibleCustomerEmployeeId().intValue())))
                .andExpect(jsonPath("responsibleEmployeeId", is(mockProject.getResponsibleEmployeeId().intValue())))
                .andExpect(jsonPath("employees", is(mockProject.getEmployees())))
                .andExpect(jsonPath("skillSet", is(mockProject.getSkillSet())))
                .andExpect(jsonPath("description", is(mockProject.getDescription())))
                .andExpect(jsonPath("startDate", is(mockProject.getStartDate().toString())))
                .andExpect(jsonPath("endDate", is(mockProject.getEndDate().toString())))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
