package io.github.shinyhappydan.contacts;

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

@WebMvcTest(Controller.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetContactsWhenEmpty() throws Exception {
        assertEquals(List.of(), getContacts());
    }

    @Test
    public void testCreateNewContact()
            throws Exception {

        var request = new Contact("Andre");

        var result = createContact(request);

        assertEquals(request.name(), result.name());
        assertNotNull(result.id());

        var contact = getContact(result.id());

        assertEquals(result, contact);
    }


    @Test
    public void testUpdateContact()
            throws Exception {

        var id = givenThereIsAContact(new Contact("Andre"));

        updateContact(id, new Contact("Penne"));

        var updatedContact = getContact(id);

        assertEquals("Penne", updatedContact.name());
    }

    @Test
    public void testDeleteContact()
            throws Exception {

        var id = givenThereIsAContact(new Contact("Andre"));

        deleteContact(id);

        assertEquals(List.of(), getContacts());
    }

    private List<ContactWithId> getContacts() throws Exception {
        var json = mvc.perform(get("/contacts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    private ContactWithId getContact(String id) throws Exception {
        var json = mvc.perform(get("/contacts/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, ContactWithId.class);
    }

    private void updateContact(String id, Contact contact) throws Exception {
        mvc.perform(post("/contacts/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk());
    }

    private void deleteContact(String id) throws Exception {
        mvc.perform(delete("/contacts/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private ContactWithId createContact(Contact contact) throws Exception {
        var json = mvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(json, ContactWithId.class);
    }

    private String givenThereIsAContact(Contact contact) throws Exception {
        return createContact(contact).id();
    }

}