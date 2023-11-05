package io.github.shinyhappydan.contacts.skills;

import io.github.shinyhappydan.contacts.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.github.shinyhappydan.contacts.Fixtures.Java6;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SkillsControllerTest {

    @Autowired
    private MockMvc mvc;

    private ApiClient client;

    @BeforeEach
    void setUp() {
        client = new ApiClient(mvc);
    }

    @Test
    public void testGetSkillsWhenEmpty() throws Exception {
        assertEquals(List.of(), client.getSkills());
    }

    @Test
    public void testCreateSkill() throws Exception {
        var skill = client.createSkill(Java6);

        assertEquals(Java6.name(), skill.name());
        assertNotNull(skill.id());

        assertEquals(skill, client.getSkill(skill.id()));
    }

    @Test
    public void testCreateSkillWithInvalidField() throws Exception {
        client.createSkillAction(new Skill("", null))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteSkill() throws Exception {
        var skill = client.createSkill(Java6);

        client.deleteSkill(skill.id());

        assertEquals(List.of(), client.getSkills());
    }

    @Test
    public void testUpdateSkill() throws Exception {
        var skill = client.createSkill(Java6);

        var updatedSkill = new Skill("Java 21", Level.INTERMEDIATE);

        var responseSkill = client.updateSkill(skill.id(), updatedSkill);

        assertEquals(updatedSkill, responseSkill.toSkill());
        assertEquals(skill.id(), responseSkill.id());

        var fetchedSkill = client.getSkill(skill.id());
        assertEquals(responseSkill, fetchedSkill);
    }
}

//@WebMvcTest(SkillsController.class)
