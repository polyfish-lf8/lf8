package de.szut.lf8_project.hello;

import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FindAllIT extends AbstractIntegrationTest {


    @Test
    void findAll() throws Exception {


        helloRepository.save(new HelloEntity("Foo"));
        helloRepository.save(new HelloEntity("Bar"));


        final var contentAsString = this.mockMvc.perform(get("/hello/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].message", is("Foo")))
                .andExpect(jsonPath("$[1].message", is("Bar")));
    }

}
