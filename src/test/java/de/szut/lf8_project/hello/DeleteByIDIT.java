package de.szut.lf8_project.hello;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DeleteByIDIT extends AbstractIntegrationTest {


    @Test
    void deleteById() throws Exception {


        HelloEntity stored = helloRepository.save(new HelloEntity("Foo"));


        final var contentAsString = this.mockMvc.perform(delete("/hello/" + stored.getId()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("message", is("Foo")));
    }


}
