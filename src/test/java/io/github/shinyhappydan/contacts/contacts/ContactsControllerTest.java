package io.github.shinyhappydan.contacts.contacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shinyhappydan.contacts.ApiClient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.github.shinyhappydan.contacts.Fixtures.Andre;
import static io.github.shinyhappydan.contacts.Fixtures.Bernie;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ContactsControllerTest {

    @Autowired
    private MockMvc mvc;

    private ApiClient client;

    @BeforeEach
    void setUp() {
        client = new ApiClient(mvc);
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateNewContact()
            throws Exception {

        var result = client.createContact(Andre);

        assertNotNull(result.id());
        assertEquals(Andre, result.toContact());

        var fetchedContact = client.getContact(result.id());

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
        client.updateContact(id, Andre.withEmail(updatedEmail));

        var updatedContact = client.getContact(id);

        assertEquals(updatedEmail, updatedContact.email());
    }

    @Test
    public void testDeleteContact()
            throws Exception {

        var id = givenThereIsAContact(Andre);

        client.deleteContact(id);

        assertEquals(List.of(), client.getContacts());
    }

    @Test
    public void testGetContactsWhenEmpty() throws Exception {
        assertEquals(List.of(), client.getContacts());
    }

    @Test
    public void testGetContacts() throws Exception {
        var andreWithId = client.createContact(Andre);
        var bernieWithId = client.createContact(Bernie);
        assertThat(client.getContacts(), Matchers.containsInAnyOrder(andreWithId, bernieWithId));
    }

    private String givenThereIsAContact(Contact contact) throws Exception {
        return client.createContact(contact).id();
    }

}