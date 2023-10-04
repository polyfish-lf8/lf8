package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.hello.HelloEntity;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindAllIT extends AbstractIntegrationTest {

    @Test
    public void authorization() throws Exception {
        this.mockMvc.perform(post("/lf8/project/findall"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void findAll() throws Exception {

        projectRepository.save(new ProjectEntity());
        projectRepository.save(new ProjectEntity());

        final var contentAsString = this.mockMvc.perform(get("/lf8/project/findall/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }
}
