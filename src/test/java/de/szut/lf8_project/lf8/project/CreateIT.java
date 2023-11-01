package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateIT extends AbstractIntegrationTest {
    @Test
    public void authorization() throws Exception {
        this.mockMvc.perform(post("/lf8/project"))
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

        this.mockMvc.perform(
                        post("/lf8/project")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
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
    public void createWithOptionalParameters() throws Exception {
        String content = """
                {
                    "customerId": 1,
                    "responsibleEmployeeId": 2,
                    "responsibleCustomerEmployeeId": 3,
                    "description": "Test description",
                    "employees": [
                        {
                            "employeeId": 1,
                            "startDate": "2023-11-01",
                            "endDate": "2023-11-02"
                        },
                        {
                            "employeeId": 2,
                            "startDate": "2023-11-01",
                            "endDate": "2023-11-02"
                        }
                    ],
                    "startDate": "2023-10-04",
                    "endDate": "2023-10-06"
                }
                """;

        this.mockMvc.perform(
                        post("/lf8/project")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("customerId", is(1)))
                .andExpect(jsonPath("responsibleEmployeeId", is(2)))
                .andExpect(jsonPath("responsibleCustomerEmployeeId", is(3)))
                .andExpect(jsonPath("description", is("Test description")))
                .andExpect(jsonPath("employees[0].employeeId", is(2)))
                .andExpect(jsonPath("employees[1].employeeId", is(1)))
                .andExpect(jsonPath("startDate", is("2023-10-04")))
                .andExpect(jsonPath("endDate", is("2023-10-06")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @WithMockUser(roles = "user")
    public void employeeNotFound() throws Exception {
        String content = """
                {
                    "customerId": 1,
                    "responsibleEmployeeId": 2,
                    "responsibleCustomerEmployeeId": 3,
                    "description": "Test description",
                    "employees": [
                        {
                            "employeeId": 1,
                            "startDate": "2023-11-01",
                            "endDate": "2023-11-02"
                        },
                        {
                            "employeeId": 2,
                            "startDate": "2023-11-01",
                            "endDate": "2023-11-02"
                        },
                        {
                            "employeeId": 3,
                            "startDate": "2023-11-01",
                            "endDate": "2023-11-02"
                        }
                    ],
                    "startDate": "2023-10-04",
                    "endDate": "2023-10-06"
                }
                """;

        this.mockMvc.perform(
                        post("/lf8/project")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                .andExpect(status().isBadRequest());
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
                            "employees": [
                                {
                                    "employeeId": 1,
                                    "startDate": "2023-11-01",
                                    "endDate": "2023-11-02"
                                },
                                {
                                    "employeeId": 2,
                                    "startDate": "2023-11-01",
                                    "endDate": "2023-11-02"
                                },
                                {
                                    "employeeId": 3,
                                    "startDate": "2023-11-01",
                                    "endDate": "2023-11-02"
                                }
                            ],
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
                            "employees": [
                                {
                                    "employeeId": 1,
                                    "startDate": "2023-11-01",
                                    "endDate": "2023-11-02"
                                },
                                {
                                    "employeeId": 1,
                                    "startDate": "2023-11-01",
                                    "endDate": "2023-11-02"
                                },
                                {
                                    "employeeId": 2,
                                    "startDate": "2023-11-01",
                                    "endDate": "2023-11-02"
                                }
                            ],
                            "startDate": "2023-10-06",
                            "endDate": "2023-10-04"
                        }
                    """
    })
    public void checkBadRequest(String content) throws Exception {
        this.mockMvc.perform(post("/lf8/project").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
