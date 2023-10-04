package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateIT extends AbstractIntegrationTest {
    @Test
    public void authorization() throws Exception {
        this.mockMvc.perform(post("/lf8/project/create"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    public void create() throws Exception {
        String content = """
                {
                    "customerId": 1,
                    "responsibleEmployeeId": 2,
                    "responsibleCustomerEmployeeId": 3,
                    "description": "Test description"
                }
                """;

        this.mockMvc.perform(post("/lf8/project/create").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("customerId", is(1)))
                .andExpect(jsonPath("responsibleEmployeeId", is(2)))
                .andExpect(jsonPath("responsibleCustomerEmployeeId", is(3)))
                .andExpect(jsonPath("description", is("Test description")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @WithMockUser(roles = "user")
    public void missingField() throws Exception {
        String content = """
                {
                    "responsibleEmployeeId": 2,
                    "responsibleCustomerEmployeeId": 3,
                    "description": "Test description"
                }
                """;

        this.mockMvc.perform(post("/lf8/project/create").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
