package io.github.shinyhappydan.contacts.contacts;

import io.github.shinyhappydan.contacts.ContactSkillStore;
import io.github.shinyhappydan.contacts.skills.SkillStore;
import io.github.shinyhappydan.contacts.skills.SkillView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
public class ContactsController {

    private final SkillStore skillStore;
    private final ContactStore contactStore;
    private final ContactSkillStore contactSkillStore;

    public ContactsController(@Autowired SkillStore skillStore, @Autowired ContactStore contactStore, @Autowired ContactSkillStore contactSkillStore) {
        this.skillStore = skillStore;
        this.contactStore = contactStore;
        this.contactSkillStore = contactSkillStore;
    }

    @GetMapping(value = "/contacts", produces = "application/json")
    public Collection<ContactWithId> getContacts() {
        return contactStore.getAll();
    }

    @GetMapping(value = "/contacts/{id}", produces = "application/json")
    public ContactWithId getContact(@PathVariable String id) {
        return requireContact(id);
    }

    private ContactWithId requireContact(String id) {
        var contact = contactStore.get(id);

        if (contact == null) throw new NoSuchElementException();
        else return contact;
    }

    @PostMapping(value = "/contacts", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactWithId createContact(@RequestBody @Valid Contact contact) {
        return contactStore.create(contact);
    }

    @PostMapping(value = "/contacts/{id}", consumes = "application/json", produces = "application/json")
    public ContactWithId updateContact(@PathVariable String id, @RequestBody Contact contact) {
        return contactStore.update(id, contact);
    }

    @DeleteMapping(value = "/contacts/{id}")
    public void deleteContact(@PathVariable String id) {
        contactStore.delete(id);
    }

    @PutMapping(value = "/contacts/{contactId}/skills/{skillId}")
    public ContactView addSkillToContact(@PathVariable String contactId, @PathVariable String skillId) {
        var contact = requireContact(contactId);
        var skill = requireSkill(skillId);
        var skills = contactSkillStore.addSkill(contact, skill);

        return contactViewFrom(contact, skills);
    }

    @DeleteMapping(value = "/contacts/{contactId}/skills/{skillId}")
    public ContactView removeSkillFromContact(@PathVariable String contactId, @PathVariable String skillId) {
        var contact = requireContact(contactId);
        var skill = requireSkill(skillId);
        var skills = contactSkillStore.removeSkill(contact, skill);

        return contactViewFrom(contact, skills);
    }

    private ContactView contactViewFrom(ContactWithId contact, Set<String> skills) {
        var skillViews = skills.stream().map(skillStore::get).collect(Collectors.toSet());
        return ContactView.from(contact, skillViews);
    }

    private SkillView requireSkill(String id) {
        var skill = skillStore.get(id);

        if (skill == null) throw new NoSuchElementException();
        else return skill;
    }
}
