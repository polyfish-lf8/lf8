package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasItem;
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

    //TODO: Einen Test der das Enddatum vor das Begindatum packt

    @Test
    @WithMockUser(roles = "user")
    public void createWithOptionalParameters() throws Exception {
        String content = """
                {
                    "customerId": 1,
                    "responsibleEmployeeId": 2,
                    "responsibleCustomerEmployeeId": 3,
                    "description": "Test description",
                    "employees": [1, 2, 3],
                    "startDate": "2023-10-04",
                    "endDate": "2023-10-06"
                }
                """;

        this.mockMvc.perform(post("/lf8/project/create").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("customerId", is(1)))
                .andExpect(jsonPath("responsibleEmployeeId", is(2)))
                .andExpect(jsonPath("responsibleCustomerEmployeeId", is(3)))
                .andExpect(jsonPath("description", is("Test description")))
                .andExpect(jsonPath("employees", hasItem(1)))
                .andExpect(jsonPath("employees", hasItem(2)))
                .andExpect(jsonPath("employees", hasItem(3)))
                .andExpect(jsonPath("startDate", is("2023-10-04")))
                .andExpect(jsonPath("endDate", is("2023-10-06")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @ParameterizedTest()
    @WithMockUser(roles = "user")
    @ValueSource(strings = {
            """
                {
                    "responsibleEmployeeId": 2,
                    "responsibleCustomerEmployeeId": 3,
                    "description": "Test description"
                }
            """,
            """
                {
                    "customerId": 1,
                    "responsibleEmployeeId": 2,
                    "responsibleCustomerEmployeeId": 3,
                    "description": "Test description",
                    "employees": [1, 2, 3],
                    "startDate": "2023-10-06",
                    "endDate": "2023-10-04"
                }
            """,
            """
                {
                    "customerId": 1,
                    "responsibleEmployeeId": 2,
                    "responsibleCustomerEmployeeId": 3,
                    "description": "Test description",
                    "employees": [1, 2, 2, 3],
                    "startDate": "2023-10-06",
                    "endDate": "2023-10-04"
                }
            """
    })
    public void checkBadRequest(String content) throws Exception {
        this.mockMvc.perform(post("/lf8/project/create").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
