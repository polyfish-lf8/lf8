package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteIT extends AbstractIntegrationTest {
    @Test
    @WithMockUser(roles = "user")
    public void DeleteProject() throws Exception {
        ProjectEntity entity = projectRepository.save(new ProjectEntity());
        String content = """
                    {
                        "customerId": 1,
                        "responsibleEmployeeId": 2,
                        "responsibleCustomerEmployeeId": 3,
                        "description": "Test description",
                        "employees": [1, 2, 2, 3],
                        "startDate": "2023-10-06",
                        "endDate": "2023-10-04"
                    }
                """;
                this.mockMvc.perform(
                 delete(String.format("/lf8/project/delete/%d", entity.getId())).content(content))
                .andExpect(status().is2xxSuccessful());
    }
}