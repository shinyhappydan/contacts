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
    private static final Skill Java6 = new Skill("Java 6");

    @Test
    public void testGetSkillsWhenEmpty() throws Exception {
        assertEquals(List.of(), getSkills());
    }

    @Test
    public void testCreateSkill() throws Exception {
        var skill = createSkill(Java6);

        assertEquals(Java6.name(), skill.name());
        assertNotNull(skill.id());

        assertEquals(skill, getSkill(skill.id()));
    }

    @Test
    public void testDeleteSkill() throws Exception {
        var skill = createSkill(Java6);

        deleteSkill(skill.id());

        assertEquals(List.of(), getSkills());
    }

    private void deleteSkill(String id) throws Exception {
        mvc.perform(delete("/skills/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSkill() throws Exception {
        var skill = createSkill(Java6);

        var updatedSkill = Java6.withName("Java 21");

        var responseSkill = updateSkill(skill.id(), updatedSkill);

        assertEquals(updatedSkill, responseSkill.toSkill());
        assertEquals(skill.id(), responseSkill.id());

        var fetchedSkill = getSkill(skill.id());
        assertEquals(responseSkill, fetchedSkill);
    }

    private SkillWithId updateSkill(String id, Skill updatedSkill) throws Exception {
        var response = mvc.perform(
                post("/skills/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSkill))
        ).andExpect(
                status().isOk()
        ).andReturn().getResponse().getContentAsString();

        return parseSkillWithId(response);
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