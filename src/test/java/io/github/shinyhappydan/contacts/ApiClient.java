package io.github.shinyhappydan.contacts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shinyhappydan.contacts.contacts.Contact;
import io.github.shinyhappydan.contacts.contacts.ContactView;
import io.github.shinyhappydan.contacts.contacts.ContactWithId;
import io.github.shinyhappydan.contacts.skills.Skill;
import io.github.shinyhappydan.contacts.skills.SkillView;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiClient {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiClient(MockMvc mvc) {

        this.mvc = mvc;
    }

    public List<ContactWithId> getContacts() throws Exception {
        var json = mvc.perform(get("/contacts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    public ContactWithId getContact(String id) throws Exception {
        var json = mvc.perform(get("/contacts/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, ContactWithId.class);
    }

    public void updateContact(String id, Contact contact) throws Exception {
        mvc.perform(post("/contacts/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk());
    }

    public void deleteContact(String id) throws Exception {
        mvc.perform(delete("/contacts/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public ContactWithId createContact(Contact contact) throws Exception {
        var json = mvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, ContactWithId.class);
    }


    public SkillView createSkill(Skill skill) throws Exception {
        return executeAndParseResponse(createSkillRequest(skill), SkillView.class, status().isCreated());
    }

    public ResultActions createSkillAction(Skill skill) throws Exception {
        return mvc.perform(createSkillRequest(skill));
    }

    public SkillView getSkill(String id) throws Exception {
        return executeAndParseResponse(get("/skills/" + id).accept(MediaType.APPLICATION_JSON), SkillView.class, status().isOk());
    }

    public List<SkillView> getSkills() throws Exception {
        var json = mvc.perform(get("/skills").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    public SkillView updateSkill(String id, Skill updatedSkill) throws Exception {
        return executeAndParseResponse(post("/skills/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSkill)), SkillView.class, status().isOk());
    }

    public void deleteSkill(String id) throws Exception {
        mvc.perform(delete("/skills/" + id))
                .andExpect(status().isOk());
    }

    private MockHttpServletRequestBuilder createSkillRequest(Skill skill) throws JsonProcessingException {
        return post("/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skill));
    }

    public ContactView addSkillToContact(String contactId, String skillId) throws Exception {
        return executeAndParseResponse(put("/contacts/" + contactId + "/skills/" + skillId), ContactView.class, status().isOk());
    }

    private <T> T executeAndParseResponse(RequestBuilder request, Class<T> parseToType, ResultMatcher...expectations) throws Exception {
        var json = mvc.perform(request)
                .andExpectAll(expectations)
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, parseToType);
    }
}
