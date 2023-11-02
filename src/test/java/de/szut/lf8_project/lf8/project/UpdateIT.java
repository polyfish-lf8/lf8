package de.szut.lf8_project.lf8.project;

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
        HashSet<Long> e = new HashSet<>(Set.of(1L, 2L));
        ProjectEntity mockProject = projectRepository.save(new ProjectEntity(2L, 3L, 4L, 5L, e, new HashSet<>(), "Hallo Waled", LocalDate.now(), LocalDate.now().plusDays(2)));

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
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id", is(mockProject.getId().intValue())))
                .andExpect(jsonPath("description", is(":)")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
