package de.szut.lf8_project.lf8.project;

import de.szut.employees.EmployeeAPI;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import okhttp3.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeAPIIT extends AbstractIntegrationTest {
    @Test
    public void getFirstEmployee() throws Exception {
        assert EmployeeAPI.getInstance().findEmployeeById(1L, token) != null;
    }

    @Test
    public void idNotFound() throws Exception {
        assert EmployeeAPI.getInstance().findEmployeeById(999L, token) == null;
    }

    @Test
    public void getAll() throws Exception {
        assert Arrays.stream(EmployeeAPI.getInstance().getAll(token)).findAny().isPresent();
    }
}
