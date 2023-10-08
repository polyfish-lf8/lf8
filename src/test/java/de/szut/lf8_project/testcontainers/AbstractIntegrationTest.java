package de.szut.lf8_project.testcontainers;

import de.szut.lf8_project.hello.HelloRepository;
import okhttp3.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * A fast slice test will only start jpa context.
 * <p>
 * To use other context beans use org.springframework.context.annotation.@Import annotation.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
@ContextConfiguration(initializers = PostgresContextInitializer.class)
public class AbstractIntegrationTest {
    protected static String token;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected HelloRepository helloRepository;

    @BeforeEach
    void setUp() {
        helloRepository.deleteAll();
    }

    @BeforeAll
    static void getToken() throws IOException {
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
}
