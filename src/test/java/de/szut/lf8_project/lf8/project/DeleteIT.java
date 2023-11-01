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
                this.mockMvc.perform(
                 delete(String.format("/lf8/project/%d", entity.getId())))
                .andExpect(status().is2xxSuccessful());
    }
}