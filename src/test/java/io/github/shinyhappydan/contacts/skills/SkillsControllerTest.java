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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    public void testCreateSkill() throws Exception {
        var skill = createSkill(new Skill("Java"));

        assertEquals("Java", skill.name());
        assertNotNull(skill.id());

        assertEquals(skill, getSkill(skill.id()));
    }

    private SkillWithId createSkill(Skill skill) throws Exception {
        var json = mvc.perform(
                post("/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skill))
        ).andExpect(
                status().isCreated()
        ).andReturn().getResponse().getContentAsString();

        return parseSkillWithId(json);
    }

    private SkillWithId getSkill(String id) throws Exception {
        var json = mvc.perform(get("/skills/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return parseSkillWithId(json);
    }

    private SkillWithId parseSkillWithId(String json) throws Exception {
        return objectMapper.readValue(json, SkillWithId.class);
    }


    private List<SkillWithId> getSkills() throws Exception {
        var json = mvc.perform(get("/skills").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }
}