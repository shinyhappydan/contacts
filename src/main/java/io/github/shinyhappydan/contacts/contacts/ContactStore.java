package io.github.shinyhappydan.contacts.contacts;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ContactStore {
    private final Map<String, ContactWithId> contacts = new HashMap<>();

    public Collection<ContactWithId> getAll() {
        return contacts.values();
    }

    public ContactWithId get(String id) {
        return contacts.get(id);
    }

    public ContactWithId create(Contact contact) {
        var id = UUID.randomUUID().toString();
        var entry = ContactWithId.from(contact, id);
        contacts.put(id, entry);
        return entry;
    }

    public ContactWithId update(String id, Contact contact) {
        var entry = ContactWithId.from(contact, id);
        if (contacts.containsKey(id)) {
            contacts.put(id, entry);
        } else {
            throw new NoSuchElementException();
        }
        return entry;
    }

    public void delete(String id) {
        contacts.remove(id);
    }
}
