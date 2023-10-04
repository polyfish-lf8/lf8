package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Create extends AbstractIntegrationTest {
    @Test
    public void authorization() throws Exception {
        this.mockMvc.perform(delete("/lf8/project/create"))
                .andExpect(status().isUnauthorized());
    }
}
