package io.github.shinyhappydan.contacts.contacts;

import io.github.shinyhappydan.contacts.ContactSkillStore;
import io.github.shinyhappydan.contacts.skills.SkillStore;
import io.github.shinyhappydan.contacts.skills.SkillView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@Tag(name = "contacts", description = "the contact API")
public class ContactsController {

    private final SkillStore skillStore;
    private final ContactStore contactStore;
    private final ContactSkillStore contactSkillStore;

    public ContactsController(@Autowired SkillStore skillStore, @Autowired ContactStore contactStore, @Autowired ContactSkillStore contactSkillStore) {
        this.skillStore = skillStore;
        this.contactStore = contactStore;
        this.contactSkillStore = contactSkillStore;
    }

    @Operation(summary = "List all contacts", description = "List all contacts that exist in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ContactWithId.class))) }),
    })
    @GetMapping(value = "/contacts", produces = "application/json")
    public Collection<ContactWithId> getContacts() {
        return contactStore.getAll();
    }

    @Operation(summary = "Get contact", description = "Look up a single contact by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContactWithId.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @GetMapping(value = "/contacts/{id}", produces = "application/json")
    public ContactWithId getContact(@PathVariable String id) {
        return requireContact(id);
    }

    @Operation(summary = "Create contact", description = "Create a new contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContactWithId.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
    })
    @PostMapping(value = "/contacts", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactWithId createContact(@RequestBody @Valid Contact contact) {
        return contactStore.create(contact);
    }

    @Operation(summary = "Update contact", description = "Update an existing contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContactWithId.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @PostMapping(value = "/contacts/{id}", consumes = "application/json", produces = "application/json")
    public ContactWithId updateContact(@PathVariable String id, @RequestBody Contact contact) {
        return contactStore.update(id, contact);
    }

    @Operation(summary = "Delete contact", description = "Delete an existing contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @DeleteMapping(value = "/contacts/{id}")
    public void deleteContact(@PathVariable String id) {
        contactStore.delete(id);
    }

    @Operation(summary = "Add skill to contact", description = "Add a skill to a contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContactView.class)) }),
            @ApiResponse(responseCode = "404", description = "Either the skill or contact was not found"),
    })
    @PutMapping(value = "/contacts/{contactId}/skills/{skillId}")
    public ContactView addSkillToContact(@PathVariable String contactId, @PathVariable String skillId) {
        var contact = requireContact(contactId);
        var skill = requireSkill(skillId);
        var skills = contactSkillStore.addSkill(contact, skill);

        return contactViewFrom(contact, skills);
    }

    @Operation(summary = "Remove skill from contact", description = "Removes a skill from a contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ContactView.class)) }),
            @ApiResponse(responseCode = "404", description = "Either the skill or contact was not found"),
    })
    @DeleteMapping(value = "/contacts/{contactId}/skills/{skillId}")
    public ContactView removeSkillFromContact(@PathVariable String contactId, @PathVariable String skillId) {
        var contact = requireContact(contactId);
        var skill = requireSkill(skillId);
        var skills = contactSkillStore.removeSkill(contact, skill);

        return contactViewFrom(contact, skills);
    }

    private ContactWithId requireContact(String id) {
        var contact = contactStore.get(id);

        if (contact == null) throw new NoSuchElementException();
        else return contact;
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
