package io.github.shinyhappydan.contacts;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class Controller {

    private final Map<String, ContactWithId> contacts = new HashMap<>();

    @GetMapping(value = "/contacts", produces = "application/json")
    public Collection<ContactWithId> getContacts() {
        return contacts.values();
    }

    @GetMapping(value = "/contacts/{id}", produces = "application/json")
    public ContactWithId getContact(@PathVariable String id) {
        return Optional.ofNullable(contacts.get(id)).orElseThrow();
    }

    @PostMapping(value = "/contacts", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactWithId createContact(@RequestBody Contact contact) {
        var id = UUID.randomUUID().toString();
        var entry = ContactWithId.from(contact, id);
        contacts.put(id, entry);
        return entry;
    }

    @PostMapping(value = "/contacts/{id}", consumes = "application/json", produces = "application/json")
    public ContactWithId updateContact(@PathVariable String id, @RequestBody Contact contact) {
        var entry = ContactWithId.from(contact, id);
        if (contacts.containsKey(id)) {
            contacts.put(id, entry);
        } else {
            throw new NoSuchElementException();
        }
        return entry;
    }

    @DeleteMapping(value = "/contacts/{id}")
    public void deleteContact(@PathVariable String id) {
        contacts.remove(id);
    }
}
