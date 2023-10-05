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
    public static String token;

    @BeforeAll
    public static void setUp() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(List.of(ConnectionSpec.MODERN_TLS))
                .build();

        Request request = new Request.Builder()
                .url("https://keycloak.szut.dev/auth/realms/szut/protocol/openid-connect/token")
                .post(RequestBody.create(MediaType.get("application/x-www-form-urlencoded"), "grant_type=password&client_id=employee-management-service&username=user&password=test"))
                .build();

        Response response = client.newCall(request).execute();
        JsonNode node = new ObjectMapper().readTree(response.body().string());
        token = "Bearer " + node.get("access_token").asText();
    }

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
