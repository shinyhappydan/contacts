package io.github.shinyhappydan.contacts.skills;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillsController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SkillsControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetSkillsWhenEmpty() throws Exception {
        assertEquals(List.of(), getSkills());
    }

    private List<String> getSkills() throws Exception {
        var json = mvc.perform(get("/skills").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }
}