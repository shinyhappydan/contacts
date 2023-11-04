package io.github.shinyhappydan.contacts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ControllerTest {

    @Autowired

    private MockMvc mvc;

    @Test
    public void testGetContactsWhenEmpty() throws Exception {
        mvc.perform(get("/contacts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(List.of())));
    }

    @Test
    public void testCreateNewContact()
            throws Exception {

        String andre = "Andre";

        mvc.perform(postContact(andre))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(andre)));

        mvc.perform(getContacts())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(List.of(andre))));
    }

    @Test
    public void testDeleteContact()
            throws Exception {

        String andre = "Andre";

        givenThereIsAContact(andre);

        mvc.perform(delete("/contacts/Andre"))
                        .andExpect(status().isOk());

        mvc.perform(getContacts())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(List.of())));
    }

    private void givenThereIsAContact(String andre) throws Exception {
        mvc.perform(postContact(andre))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(andre)));
    }

    private static MockHttpServletRequestBuilder postContact(String name) {
        return post("/contacts").contentType(MediaType.APPLICATION_JSON).content(name);
    }

    private MockHttpServletRequestBuilder getContacts() {
        return get("/contacts").contentType(MediaType.APPLICATION_JSON);
    }
}