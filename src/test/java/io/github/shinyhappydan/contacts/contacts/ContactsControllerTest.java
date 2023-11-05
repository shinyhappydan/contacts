package io.github.shinyhappydan.contacts.contacts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactsController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ContactsControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Contact Andre = new Contact("Andre", "O'Connor", "Andre O'Connor", "12 Bonio Lane", "andre@rules.ok", "07755667788");
    private static final Contact Bernie = new Contact("Bernie", "Bell-Bamford", "Bernie Bell-Bamford", "Pat's Paws", "bernie@zoomies.org", "077666778899");

    @Test
    public void testCreateNewContact()
            throws Exception {

        var result = createContact(Andre);

        assertNotNull(result.id());
        assertEquals(Andre, result.toContact());

        var fetchedContact = getContact(result.id());

        assertEquals(result, fetchedContact);
    }

    @Test
    public void testCreateNewContactWithInvalidFields()
            throws Exception {

        var contact = new Contact("", "", "", "", "ziggy!barcelonafc.com", "123A567890");

        mvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateContact()
            throws Exception {

        var id = givenThereIsAContact(Andre);

        String updatedEmail = "andre@good.boy";
        updateContact(id, Andre.withEmail(updatedEmail));

        var updatedContact = getContact(id);

        assertEquals(updatedEmail, updatedContact.email());
    }

    @Test
    public void testDeleteContact()
            throws Exception {

        var id = givenThereIsAContact(Andre);

        deleteContact(id);

        assertEquals(List.of(), getContacts());
    }

    @Test
    public void testGetContactsWhenEmpty() throws Exception {
        assertEquals(List.of(), getContacts());
    }

    @Test
    public void testGetContacts() throws Exception {
        var andreWithId = createContact(Andre);
        var penneWithId = createContact(Bernie);
        assertThat(getContacts(), Matchers.containsInAnyOrder(andreWithId, penneWithId));
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